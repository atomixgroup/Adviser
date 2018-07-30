<?php

namespace App\Http\Controllers;

use App\Content;
use App\Match;
use App\Permium;
use App\Category;
use App\User;
use App\Rate;
use GuzzleHttp\Client;
use Illuminate\Http\Request;

class Controller_Content extends Controller
{
    private $match_time = 8;

//    function insert(){
//        $path="uploads/";
//        if(file_exists($path)){
//            for ($i=1;$i<14;$i++){
//                $fpath=$path.$i;
//                for ($z=0;$z<80;$z++){
//                    $zpath=$fpath."/".$z;
//                    if(file_exists($zpath.".ogg") or file_exists($zpath."-1.ogg")){
//                        $content=new Content();
//                        if($z==0){
//                            $content->title="فایل صوتی"." نسخه معرفی(رایگان)";
//                        }
//                        else{
//                            $content->title="فایل صوتی"." ".$z;
//                        }
//                        $content->g_id=$i;
//                        $content->price=0;
//                        $content->section=$z;
//                        $content->save();
//                    }
//                }
//            }
//        }
//    }
    function getContents(Request $request)
    {
        $contents = Content::orderBy("section","asc")->get();
        for ($i=0;$i<count($contents);$i++){
            $cat=Category::where("id",$contents[$i]->g_id)->first();
            $contents[$i]->content=$cat->content;
        }
        $contents=$contents->toArray();
        $user=false;
        if($request->has("userId")){
            $user = User::where("userId", $request->input("userId"))->first();
        }
        if ($user) {
            for ($i = 0; $i < count($contents); $i++) {
                $permium = Permium::where("u_id", $user->id)->where("c_id", $contents[$i]["id"])->where("status", "paid")->where("content_type","content")->first();
                if ($permium) {
                    $contents[$i]['premium'] = "yes";

                } else {
                    $permium = Permium::where("u_id", $user->id)->where("c_id", $contents[$i]["g_id"])->where("status", "paid")->where("content_type","category")->first();
                    if(!$permium){
                        $contents[$i]['premium'] = "no";
                    }
                    else{
                        $contents[$i]['premium'] = "yes";
                    }
                }

            }
        } else {
            for ($i = 0; $i < count($contents); $i++) {
                $contents[$i]['premium'] = "no";

            }
        }
        $categories = Category::orderBy("id","asc")->get();
        for($i=0;$i<count($categories);$i++){
            $categories[$i]->price=$categories[$i]->getPrice();
        }

        foreach ($contents as $content){
            if($content['premium']=="yes"){
                for ($i=0;$i<count($categories);$i++){
                    if($content['g_id']==$categories[$i]->id){
                        $categories[$i]->price=$categories[$i]->price-intval($content['price']);
                    }
                }
            }
        }

        $categories=$categories->toArray();
        $data = array(
            $categories,
            $contents,

        );
        return response()->json($data);


    }

    function getWallet(Request $request)
    {
        $user = User::where("userId", $request->input("userId"))->first();
        return response($user->wallet);
    }

    function getContent(Request $request)
    {
        $user = User::where("userId", $request->input("userId"))->first();

        $content = Content::where("id", $request->input("id"))->first()->toArray();

        if ($user) {
            $permium = Permium::where("u_id", $user->id)->where("c_id", $content['id'])->where("status", "paid")->first();
            // print_r($permium);
            // return;
            if (!$permium) {
                $content['premium'] = "no";
            } else {
                $content['premium'] = "yes";
            }
            $aCount=count(explode(",",$content["audio"]));
            $content['audio_count'] = $aCount;
            unset($content['audio']);
        } else {
            $content['premium'] = "no";
            $aCount=count(explode(",",$content["audio"]));
            $content['audio_count'] = $aCount;
            unset($content['audio']);
        }
        return response(json_encode($content));
    }

    function setRate(Request $request)
    {
        if ($request->has(["IMEI", "rate"])) {
            $IMEI = $request->input("IMEI");
            $rate = intval($request->input("rate"));
            $id = $request->input("id");
            $rateModel = Rate::where("p_id", $id)->where("IMEI", $IMEI)->first();
            if ($rateModel) {
                $rateModel->rate = $rate;
                $rateModel->save();
            } else {
                $rateModel = new Rate();
                $rateModel->p_id = $id;
                $rateModel->rate = $rate;
                $rateModel->IMEI = $IMEI;
                $rateModel->save();
            }
            echo "OK:1";
        } else {
            echo "ERROR:1";
        }
    }

    function getAvrage(Request $request)
    {
        if ($request->has("id")) {
            $id = $request->input("id");

            $rates = Rate::where("p_id", $id)->get();
            $sum = 0;
            if (count($rates) > 0) {
                foreach ($rates as $rate) {
                    $sum += $rate->rate;
                }
                $avrage = $sum / count($rates);
                echo $avrage;
            } else {
                echo "0";
            }
        }
    }

    function getSound(Request $request)
    {
        $id = $request->input("id");
        $userId=$request->input("userId");
        if($userId and $userId!=""){
            $user = User::where("userId", $userId)->first();
        }
        $content = Content::where("id", $id)->first();
        if($content->price==0){

            // return response(url("") . "/uploads/".$urls[$pos]);
            $fpath="uploads/".$content->g_id;
            $zpath=$fpath."/".$content->section;
            if(file_exists($zpath.".ogg")){

                $headers = [
                    'Content-Type' => 'application/ogg',
                ];

                return response()->download("uploads/$content->g_id/$content->section.ogg","$content->section" , $headers);
            }
        }
        else{
            if ($user) {
                $permium = Permium::where("u_id", $user->id)->where("c_id", $content->id)->where("status", "paid")->where("content_type","content")->first();
                if ($permium) {
                    return response()->download("uploads/$content->g_id/$content->section.ogg");
                }
                else{
                    $permium = Permium::where("u_id", $user->id)->where("c_id", $content->g_id)->where("status", "paid")->where("content_type","category")->first();
                    if($permium){
                        return response()->download("uploads/$content->g_id/$content->section.ogg");
                    }
                    else{
                        return response(url(""));
                    }
                }
            }
        }

        return response("");
    }

    function setToken(Request $request)
    {
        $user = User::where("imei", $request->input("imei"))->first();
        if ($user) {
            $user->token = $request->input("token");
        } else {
            $user = new User();
            $user->imei = $request->input("imei");
            $user->token = $request->input("token");
        }
        $user->save();
    }

    function logout(Request $request)
    {
        $request->session()->forget('user');
        return redirect()->route("root");

    }
    function login(Request $request){
        return view("backend.login");
    }
    function loginSet(Request $request)

    {
        $username = $request->input("username");
        $password = $request->input("password");
        $token = $request->input('g-recaptcha-response');

        if ($token) {
            $client = new Client(array('curl' => array(CURLOPT_SSL_VERIFYPEER => false,),));
            $response = $client->post('https://www.google.com/recaptcha/api/siteverify', [
                'form_params' => array(
                    'secret' => '6LdcQkgUAAAAAP_2mC-9dw5pJugZJiIHqlXZE_6E',
                    'response' => $token
                )
            ]);
            $results = json_decode($response->getBody()->getContents());

            if ($results->success) {

                if ($username == "admin" && $password == "admin") {
                    session(['user' => array(
                        "user" => "admin admin ",
                        "rule" => "admin",
                        "id" => 1,
                        "logged" => "true"
                    )]);
                    return redirect()->route("content.index");
                }
                return redirect()->route("login");
            }
            else{
                return redirect()->route("login");
            }
        } else {
////                Session::flash('error', 'You are probably a robot!');
            return redirect()->route("login");
        }
        # we know it was submitted


    }
    function apiActive(Request $request){
        $user=User::where("userId",$request->input("userId"))->where("activeCode",$request->input("code"))->first();
        if($user){
            $user->userId=$this->getHashString($user->mobile);
            $user->save();
            return response($user->userId);
        }
        else{
            return response("ERROR:0");
        }
    }
    function convert($string) {
    $persian = ['۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'];
    $arabic = ['٩', '٨', '٧', '٦', '٥', '٤', '٣', '٢', '١','٠'];

    $num = range(0, 9);
    $convertedPersianNums = str_replace($persian, $num, $string);
    $englishNumbersOnly = str_replace($arabic, $num, $convertedPersianNums);

    return $englishNumbersOnly;
}
    function apiLogin(Request $request){
        $mobile=$request->input("mobile");
        $mobile=$this->convert($mobile);
        $activeCode=rand(1000,9999);
        $user=User::where("mobile",$mobile)->first();
        if(!$user){
            $user=new User();
            $user->mobile=$mobile;
            $user->wallet=0;
            $user->userId=$this->getHashString($mobile);
            $user->activeCode=$activeCode;
            $user->save();
        }
        else{
            $user->mobile=$mobile;
            $user->activeCode=$activeCode;
            $user->save();
        }
        $this->sendSms($mobile,"کد فعالسازی برنامه ققنوس : ".$activeCode);
        return response($user->userId);

    }
    private
        $BASE_HTTP_URL = "http://www.sibsms.com/APISend.aspx?";
    function sendSms($mobile,$text){

        $USERNAME = "09143322996";  // your username (fill it with your username)
        $PASSWORD = "63266741"; // your password (fill it with your password)        $senderNumber = "30007546"; // [FILL] sender number ; which is your 3000xxx number
        $senderNumber = "50002030003888";
        $message = urlencode($text); // [FILL] the content of the message; (in url-encoded format !)

        // creating the url based on the information above
        $url = $this->BASE_HTTP_URL .
            "Username=" . $USERNAME . "&Password=" . $PASSWORD .
            "&From=" . $senderNumber . "&To=" . $mobile .
            "&Text=" . $message;
        // sending the request via http call
        $result = $this->call($url);
        // Now you can compare the response with 0 or 1
    }
    private
    function call($url)
    {
        return file_get_contents($url);
    }
    function getHashString($text){
        return md5($text+md5($text.sha1($text)).time());
    }
}
