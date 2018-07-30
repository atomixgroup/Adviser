<?php

namespace App\Http\Controllers;

use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Routing\Controller as BaseController;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use stdClass;

class Controller extends BaseController
{
    public $data;
    public function __construct()
    {
        $this->data=new stdClass();
    }
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;
}
