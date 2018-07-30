<?php

namespace App\Http\Controllers;


use App\Category;
use App\Content;
use App\Http\Controllers\Controller;


use App\Permium;
use App\User;
use Illuminate\Http\Request;

use SoapClient;


class Controller_Payment extends Controller
{
    private $MerchantID = 'd03cde10-61e5-11e8-a711-005056a205be';

    function PayRequest(Request $request)
    {

        $user = User::where("userId", $request->input("userId"))->first();
        if(!$user) return response("ERROR:1");
        $phone = "09308144474";
        $code = "منیره محسن زاده";


//        	amount	u_id	ref_id	authority	type	region_code	products	address	postal	phone	status

        $Description = $code;  // Required
        $Email = 'raminsk8er@gmail.com'; // Optional
        $Mobile = $phone; // Optional
        $CallbackURL = route("payment.back");  // Required

        // URL also can be ir.zarinpal.com or de.zarinpal.com
        $client = new SoapClient('https://www.zarinpal.com/pg/services/WebGate/wsdl', ['encoding' => 'UTF-8']);

        if ($request->input("type") == "wallet") {
            $amount = $request->input("price");
            $result = $client->PaymentRequest([
                'MerchantID' => $this->MerchantID,
                'Amount' => $amount,
                'Description' => $Description,
                'Email' => $Email,
                'Mobile' => $Mobile,
                'CallbackURL' => $CallbackURL,
            ]);
            //Redirect to URL You can do it also by creating a form
            if ($result->Status == 100) {
                $premium = new Permium();
                $premium->price = $amount;
                $premium->u_id = $user->id;
                $premium->c_id = 0;
                $premium->type = "wallet";
                $premium->content_type="wallet";
                $premium->authority = $result->Authority;
                $premium->status = "cashPending";
                $premium->save();
                return redirect('https://www.zarinpal.com/pg/StartPay/' . $result->Authority);
            } else {
                echo $result->Status;
            }
        } else {
            if ($request->has("set") and $request->input("set") == "category") {
                $category = Category::where("id", $request->input("id"))->first();
                $price = $category->getPrice();
                $price = floatval($price) - floatval($price * 15 / 100);
                $price /= 100;
                $price = floor($price);
                $price *= 100;
                $price += 100;
            } else {
                $content = Content::where("id", $request->input("id"))->first();
                $price = $content->price;
            }

            $amount = $price;
            $result = $client->PaymentRequest([
                'MerchantID' => $this->MerchantID,
                'Amount' => $amount,
                'Description' => $Description,
                'Email' => $Email,
                'Mobile' => $Mobile,
                'CallbackURL' => $CallbackURL,
            ]);
            //Redirect to URL You can do it also by creating a form
            if ($result->Status == 100) {
                if ($request->has("set") and $request->input("set") == "category") {
                    $premium = new Permium();
                    $premium->price = $amount;
                    $premium->u_id = $user->id;
                    $premium->c_id = $category->id;
                    $premium->type = "realtime";
                    $premium->content_type = "category";
                    $premium->authority = $result->Authority;
                    $premium->status = "cashPending";
                    $premium->save();
                    return redirect('https://www.zarinpal.com/pg/StartPay/' . $result->Authority);

                } else {
                    $premium = new Permium();
                    $premium->price = $amount;
                    $premium->u_id = $user->id;
                    $premium->c_id = $content->id;
                    $premium->type = "realtime";
                    $premium->content_type = "content";
                    $premium->authority = $result->Authority;
                    $premium->status = "cashPending";
                    $premium->save();
                    return redirect('https://www.zarinpal.com/pg/StartPay/' . $result->Authority);
                }

            } else {
                echo $result->Status;
            }
        }
        return;
    }

//u_id	message	status	type	value

    function PayBack(Request $request)
    {
        //Amount will be based on Toman
        $Authority = $request->input('Authority');
        $status = $request->input("Status");
        if ($status == 'OK') {
            $premium = Permium::where('authority', $Authority)->first();

            if ($premium) {
                $Amount = $premium->price;
                $client = new SoapClient('https://www.zarinpal.com/pg/services/WebGate/wsdl', ['encoding' => 'UTF-8']);
                $result = $client->PaymentVerification([
                    'MerchantID' => $this->MerchantID,
                    'Authority' => $Authority,
                    'Amount' => $Amount,
                ]);

                if ($result->Status == 100 and $premium->status!="paid") {
                    $premium->ref_id = $result->RefID;
                    $premium->status = "paid";
                    $premium->save();
                    if ($premium->type = "wallet") {
                        $user = User::where("id", $premium->u_id)->first();
                        $user->wallet += intval($Amount);
                        $user->save();
                    }

                    return view("transaction")->with("ref", $result->RefID)->with("status", "1");
                } else {
                    return view("transaction")->with("status", "2");

                }
            }
        } else {
            return view("transaction")->with("status", "3");
        }
    }

    function PayWalletRequest(Request $request)
    {
        $user = User::where("userId", $request->input("userId"))->first();

        if ($user) {
            if($request->has("id")){
                $content = Content::where("id", $request->input("id"))->first();
                if ($content) {
                    if (intval($user->wallet) >= intval($content->price)) {
                        $premium = new Permium();
                        $premium->price = $content->price;
                        $premium->u_id = $user->id;
                        $premium->c_id = $content->id;
                        $premium->type = "wallet";
                        $premium->content_type="content";
                        $premium->authority = "";
                        $premium->status = "paid";
                        $premium->save();
                        $user->wallet = intval($user->wallet) - intval($content->price);
                        $user->save();
                        return response("OK:1");

                    } else {
                        return response("ERROR:-1");
                    }
                } else {
                    return response("ERROR:-2");

                }
            }elseif($request->has("g_id")){
                $price=0;
                $contents=Content::where("g_id",$request->input("g_id"))->get();
                foreach ($contents as $content){
                    $permium = Permium::where("u_id", $user->id)->where("c_id", $content->id)->where("status", "paid")->where("content_type","content")->first();
                    if(!$permium){
                        $price+=$content->price;
                    }
                }
                $price = floatval($price) - floatval($price * 15 / 100);
                $price /= 100;
                $price = floor($price);
                $price *= 100;
                $price += 100;
                if (intval($user->wallet) >= $price) {
                    $premium = new Permium();
                    $premium->price = $price;
                    $premium->u_id = $user->id;
                    $premium->c_id = $request->input("g_id");
                    $premium->type = "wallet";
                    $premium->content_type = "category";
                    $premium->authority = "";
                    $premium->status = "paid";
                    $premium->save();
                    $user->wallet = intval($user->wallet) - intval($content->price);
                    $user->save();
                    return response("OK:1");
                }else {
                    return response("ERROR:-1");
                }
            }
            

        } else {
            return response("ERROR:0");

        }
    }

}
