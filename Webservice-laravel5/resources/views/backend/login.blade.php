
<!DOCTYPE html>
<html lang="en" dir="rtl">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="پنل مدیریت">
    <meta name="author" content="سرویس">

    <!-- App Favicon -->
    <link rel="shortcut icon" href="{{url("backend")}}/images/favicon.ico">

    <!-- App title -->
    <title>پنل مدیریت مطالب</title>

    <!-- App CSS -->
    <link href="{{url("backend")}}/style/bootstrap-rtl.min.css" rel="stylesheet" type="text/css" />
    <link href="{{url("backend")}}/style/core.css" rel="stylesheet" type="text/css" />
    <link href="{{url("backend")}}/style/components.css" rel="stylesheet" type="text/css" />
    <link href="{{url("backend")}}/style/icons.css" rel="stylesheet" type="text/css" />
    <link href="{{url("backend")}}/style/pages.css" rel="stylesheet" type="text/css" />
    <link href="{{url("backend")}}/style/menu.css" rel="stylesheet" type="text/css" />
    <link href="{{url("backend")}}/style/responsive.css" rel="stylesheet" type="text/css" />

    <!-- HTML5 Shiv and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

    <script src="{{url("backend")}}/js/modernizr.min.js"></script>
    <script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body>

<div class="account-pages"></div>
<div class="clearfix"></div>
<div class="wrapper-page">
    <div class="text-center">
        <a href="index.html" class="logo"><span>پنل<span>مدیریت</span></span></a>
        <h5 class="text-muted m-t-0 font-600">سرویس مطالب</h5>
    </div>
    <div class="m-t-40 card-box">
        <div class="text-center">
            <h4 class="text-uppercase font-bold m-b-0">ورود</h4>
        </div>
        <div class="panel-body">
            <form class="form-horizontal m-t-20" action="{{route("login")}}" method="post">
                {{csrf_field()}}
                <div class="form-group ">
                    <div class="col-xs-12">
                        <input name="username" class="form-control" type="text" required="" placeholder="نام کاربری">
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-12">
                        <input name="password" class="form-control" type="password" required="" placeholder="رمز عبور">
                    </div>
                </div>

                <div class="form-group ">
                    <div class="col-xs-12">
                        <div class="checkbox checkbox-custom">
                            <div class="g-recaptcha"
                                 data-sitekey="6LdcQkgUAAAAAO41T5-VxTUpObQ6K7Lov20NGj8u"></div>
                        </div>

                    </div>
                </div>

                <div class="form-group text-center m-t-30">
                    <div class="col-xs-12">
                        <button class="btn btn-custom btn-bordred btn-block waves-effect waves-light" type="submit">ورود</button>
                    </div>
                </div>

            </form>

        </div>
    </div>
    <!-- end card-box-->


</div>
<!-- end wrapper page -->



<script>
    var resizefunc = [];
</script>

<!-- jQuery  -->
<script src="{{url("backend")}}/js/jquery.min.js"></script>
<script src="{{url("backend")}}/js/bootstrap-rtl.min.js"></script>
<script src="{{url("backend")}}/js/detect.js"></script>
<script src="{{url("backend")}}/js/fastclick.js"></script>
<script src="{{url("backend")}}/js/jquery.slimscroll.js"></script>
<script src="{{url("backend")}}/js/jquery.blockUI.js"></script>
<script src="{{url("backend")}}/js/waves.js"></script>
<script src="{{url("backend")}}/js/wow.min.js"></script>
<script src="{{url("backend")}}/js/jquery.nicescroll.js"></script>
<script src="{{url("backend")}}/js/jquery.scrollTo.min.js"></script>

<!-- App js -->
<script src="{{url("backend")}}/js/jquery.core.js"></script>
<script src="{{url("backend")}}/js/jquery.app.js"></script>

</body>
</html>