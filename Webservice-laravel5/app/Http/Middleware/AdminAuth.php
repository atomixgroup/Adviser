<?php

	namespace App\Http\Middleware;

	use Closure;

	class AdminAuth
	{
		/**
		 * Run the request filter.
		 *
		 * @param  \Illuminate\Http\Request  $request
		 * @param  \Closure  $next
		 * @return mixed
		 */
		public function handle($request, Closure $next)
        {

            $value = $request->session()->get('user');
            if (($value['rule'] == "admin" ) && $value['logged'] == "true") {
                return $next($request);
            } else {
                return redirect()->route("login");
            }
        }

	}