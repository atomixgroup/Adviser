<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Category extends Model
{
    protected $table="category";
    public $timestamps=false;
    public function getPrice()
    {
        $price=0;
        $contents=Content::where("g_id",$this->id)->get();
        foreach ($contents as $content){
            $price+=intval($content->price);
        }
        return $price;
    }
}
