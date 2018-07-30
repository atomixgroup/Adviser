<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
Route::post('getAvrage', "Controller_Content@getAvrage");
Route::post('setrate', "Controller_Content@setRate");
Route::post('getContents', "Controller_Content@getContents");
Route::post('getContent', "Controller_Content@getContent");
Route::get('getSound', "Controller_Content@getSound");

Route::post('getQuestions', "Controller_Content@getQuestions");
Route::post('getTimer', "Controller_Content@getTimer");
Route::post('setMatch', "Controller_Content@setMatch");
Route::post('setToken', "Controller_Content@setToken");
Route::get('premiumRequest', "Controller_Payment@PayRequest");
Route::post('premiumWalletRequest', "Controller_Payment@PayWalletRequest");
Route::post('getWallet', "Controller_Content@getWallet");
Route::get("/back", ['as' => 'payment.back', 'uses' => 'Controller_Payment@PayBack']);

Route::post('setLogin', "Controller_Content@apiLogin");
Route::post('setActive', "Controller_Content@apiActive");