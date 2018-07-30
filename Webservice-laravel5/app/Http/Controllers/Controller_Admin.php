<?php

namespace App\Http\Controllers;

use App\Cart;
use App\CheckOut;
use App\Installer;
use App\Notif;
use App\Reseller;
use App\Setting;
use Hekmatinasser\Verta\Verta;
use Illuminate\Http\Request;
use App\Category;
use App\Content;
use App\Service;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Input;

class Controller_Admin extends Controller
{
    private $status = array(
        
        "deactivate" => "غیر فعال",
        "active" => "فعال",

    );
    function __construct()
    {
        parent::__construct();
       
    }
    function contentIndex(Request $request)
    {
        $this->data->title = "مطالب";
        $contents = Content::orderBy('id', 'DESC')->get();;
        $statuses = array();
        foreach ($this->status as $key => $val) {
            $statuses[] = array("value" => $key, "text" => $val);
        }
        $this->data->statuses = $statuses;

        for($i=0;$i<count($contents);$i++){
            $content=$contents[$i];
            $content->status=$this->status[$content->status];
            $contents[$i]=$content;
        }
        $this->data->contents = $contents;
        return view("backend.admin.content.index")->withData($this->data);
    }


    function contentEdit(Request $request)
    {
        $this->data->title = "ویرایش مطالب";

        if ($request->has("submit")) {

            $filePath = 'uploads';
            $filename = "";

            $content = null;
            if ($request->has("id")) {
                $content = Content::where("id", $request->input("id"))->first();

            }
            if (!$content) {
                $content = new Content();
            }
            $content->title = $request->input("title");
            $content->body = $request->input("body");
            $content->price = $request->input("price");
            $content->status = $request->input("status");
            $content->g_id = $request->input("g_id");

            $content->save();
            if(!$request->has("id")){
                $content->audio="";
            }
            if (Input::hasFile('file1')) {
                $file = Input::file('file1');
                $filename = $file->getClientOriginalName();
                $ext = pathinfo($filePath . "/" . $filename, PATHINFO_EXTENSION);
                $p=$content->g_id."-file1-" . $content['id'] . "-" . time() . "." . $ext;
                $file->move($filePath, $p);
                $content->video.= $filePath . "/" . $p.",";
            }
            if (Input::hasFile('file2')) {
                $file = Input::file('file2');
                $filename = $file->getClientOriginalName();
                $ext = pathinfo($filePath . "/" . $filename, PATHINFO_EXTENSION);
                $p=$content->g_id."-file2-" . $content['id'] . "-" . time() . "." . $ext;
                $file->move($filePath, $p);
                $content->video.= $filePath . "/" . $p.",";
            }
            if (Input::hasFile('file3')) {
                $file = Input::file('file3');
                $filename = $file->getClientOriginalName();
                $ext = pathinfo($filePath . "/" . $filename, PATHINFO_EXTENSION);
                $p=$content->g_id."-file3-" . $content['id'] . "-" . time() . "." . $ext;
                $file->move($filePath, $p);
                $content->video.= $filePath . "/" . $p.",";
            }
            if (Input::hasFile('file4')) {
                $file = Input::file('file4');
                $filename = $file->getClientOriginalName();
                $ext = pathinfo($filePath . "/" . $filename, PATHINFO_EXTENSION);
                $p=$content->g_id."-file4-" . $content['id'] . "-" . time() . "." . $ext;
                $file->move($filePath, $p);
                $content->video.= $filePath . "/" . $p.",";
            }
            if (Input::hasFile('file5')) {
                $file = Input::file('file5');
                $filename = $file->getClientOriginalName();
                $ext = pathinfo($filePath . "/" . $filename, PATHINFO_EXTENSION);
                $p=$content->g_id."-file5-" . $content['id'] . "-" . time() . "." . $ext;
                $file->move($filePath, $p);
                $content->video.= $filePath . "/" . $p;
            }
            $content->save();
            return redirect()->route("content.index");
        } else {
            $this->data->categories=Category::all();
            if ($request->has("id")) {
                $this->data->content = Content::where("id", $request->input("id"))->first();
                return view("backend.admin.content.edit")->withData($this->data);
            } else {
                return view("backend.admin.content.edit")->withData($this->data);
            }
        }

    }

    function contentDelete(Request $request)
    {
        $content = Content::where("id", $request->input("id"))->first();
        if ($content) {
            $content->delete();
        }
        return redirect()->route("content.index");
    }

   

    function categoryIndex(Request $request)
    {
        $this->data->title = "لیست دوره ها";

        $categories = Category::orderBy('id', 'DESC')->get();;

        $this->data->categories = $categories;
        return view("backend.admin.category.index")->withData($this->data);
    }

    function categoryEdit(Request $request)
    {
        $this->data->title = "ویرایش دوره";

        if ($request->has("submit")) {

            $category = null;
            if ($request->has("id")) {
                $category = Category::where("id", $request->input("id"))->first();
            }
            if (!$category) {
                $category = new Category();
            }
            
            $category->title = $request->input("title");
            $category->save();

            return redirect()->route("category.index");
        } else {
            if ($request->has("id")) {
                $this->data->category = Category::where("id", $request->input("id"))->first();
            }
            return view("backend.admin.category.edit")->withData($this->data);
        }

    }

    function categoryDelete(Request $request)
    {
        $category = Category::where("id", $request->input("id"))->first();
        if ($category) {
            $category->delete();
        }
        return redirect()->route("category.index");
    }



   

}
