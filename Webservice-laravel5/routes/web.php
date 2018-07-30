<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/
 Route::get('insert','Controller_Content@insert');
Route::group(['prefix' => 'admin','middleware' => 'admin_auth'], function () {
    Route::get('/', [
        'uses' => 'Controller_Admin@contentIndex'
    ]);
    Route::get('dashboard', [
        'uses' => 'Controller_Admin@contentIndex'
    ]);
    
    Route::get('content', [
        'as' => 'content.index', 'uses' => 'Controller_Admin@contentIndex'
    ]);
    Route::get('content/register', [
        'as' => 'content.register', 'uses' => 'Controller_Admin@contentEdit'
    ]);
    Route::post('content/register', [
        'as' => 'content.register', 'uses' => 'Controller_Admin@contentEdit'
    ]);
    Route::get('content/delete', [
        'as' => 'content.delete', 'uses' => 'Controller_Admin@contentDelete'
    ]);
    

    Route::get('category', [
        'as' => 'category.index', 'uses' => 'Controller_Admin@categoryIndex'
    ]);
    Route::get('category/register', [
        'as' => 'category.register', 'uses' => 'Controller_Admin@categoryEdit'
    ]);
    Route::post('category/register', [
        'as' => 'category.register', 'uses' => 'Controller_Admin@categoryEdit'
    ]);
    Route::get('category/delete', [
        'as' => 'category.delete', 'uses' => 'Controller_Admin@categoryDelete'
    ]);

   

});
Route::post('login', [
    'as' => 'login', 'uses' => 'Controller_Content@loginSet'
]);
Route::get('logout', [
    'as' => 'logout', 'uses' => 'Controller_Content@logout'
]);
Route::get('login', [
    'as' => 'login', 'uses' => 'Controller_Content@login'
]);