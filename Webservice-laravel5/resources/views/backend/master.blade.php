<!DOCTYPE html>
<html lang="en" dir="rtl">

@include("backend.header")

<body class="fixed-left">

<!-- Begin page -->
<div id="wrapper">

    <!-- Top Bar Start -->
    <div class="topbar">

        <!-- LOGO -->
        <div class="topbar-left">
            <a href="index.html" class="logo"><span>تابا<span>نت</span></span><i class="zmdi zmdi-layers"></i></a>
        </div>

        <!-- Button mobile view to collapse sidebar menu -->
        <div class="navbar navbar-default" role="navigation">
            <div class="container">

                <!-- Page title -->
                <ul class="nav navbar-nav navbar-left">
                    <li>
                        <button class="button-menu-mobile open-left">
                            <i class="zmdi zmdi-menu"></i>
                        </button>
                    </li>
                    <li>
                        <h4 class="page-title">{{(isset($data->title))?$data->title:"تابا"}}</h4>
                    </li>
                </ul>

                <!-- Right(Notification and Searchbox -->
                <ul class="nav navbar-nav navbar-right">
                    @if(isset($data->notifs))
                        <li>
                            <!-- Notification -->
                            <div class="notification-box">
                                <ul class="list-inline m-b-0">
                                    <li>
                                        <a href="javascript:void(0);" class="right-bar-toggle">
                                            <i class="zmdi zmdi-notifications-none"></i>
                                        </a>
                                        <div class="noti-dot">
                                            @if(isset($data->notifs) and count($data->notifs)>0)
                                                <span class="dot"></span>
                                                <span class="pulse"></span>
                                            @endif
                                        </div>
                                        <div id="sound">

                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <!-- End Notification bar -->
                        </li>
                    @endif
                    @yield("admin.search")
                </ul>

            </div><!-- end container -->
        </div><!-- end navbar -->
    </div>
    <!-- Top Bar End -->


    <!-- ========== Left Sidebar Start ========== -->
    <div class="left side-menu">
        <div class="sidebar-inner slimscrollleft">

            <!-- User -->
            <div class="user-box">
                <div class="user-img">
                    <img src="{{url("backend/images")}}/avatar-1.jpg" alt="user-img" title="ای تمز"
                         class="img-circle img-thumbnail img-responsive">
                    <div class="user-status offline"><i class="zmdi zmdi-dot-circle"></i></div>
                </div>

                <ul class="list-inline">

                    <li>
                        <a href="{{route("logout")}}" class="text-custom">
                            <i class="zmdi zmdi-power"></i>
                        </a>
                    </li>
                </ul>
            </div>
            <!-- End User -->

            <!--- Sidemenu -->
            <div id="sidebar-menu">
                @yield("menu")
                <div class="clearfix"></div>
            </div>
            <!-- Sidebar -->
            <div class="clearfix"></div>

        </div>

    </div>
    <!-- Left Sidebar End -->


    <!-- ============================================================== -->
    <!-- Start right Content here -->
    <!-- ============================================================== -->
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">

                @yield("content")

            </div> <!-- container -->

        </div> <!-- content -->

    @include("backend.footer")

    <!-- ============================================================== -->
        <!-- End Right content here -->
        <!-- ============================================================== -->


        <!-- Right Sidebar -->
        <div class="side-bar right-bar">
            <a href="javascript:void(0);" class="right-bar-toggle">
                <i class="zmdi zmdi-close-circle-o"></i>
            </a>
            <h4 class="">اطلاعیه ها</h4>
            <div class="notification-list nicescroll">
                <ul id="notif_list" class="list-group list-no-border user-list">
                    @if(isset($data->notifs))

                        @foreach($data->notifs as $notif)

                            <li class="list-group-item">
                                <a href="{{route("notif.click",["id"=>$notif->id])}}" class="user-list-item">
                                    <div class="icon bg-info">
                                        <i class="zmdi zmdi-account"></i>
                                    </div>
                                    <div class="user-desc">
                                        <span class="name">{{$notif->title}}</span>
                                        <span class="desc">{{$notif->message}}</span>
                                        <span class="time">{{new Verta($notif->created_at)}}</span>
                                    </div>
                                </a>
                            </li>
                        @endforeach
                        @endif

                </ul>
            </div>
        </div>
        <!-- /Right-bar -->

    </div>
    <!-- END wrapper -->

    @if(isset($data->notifs))
        <script>
            $(function (e) {
                setInterval(function () {
                    $.get("{{route("get.notifs")}}",
                        {},
                        function (data, status) {

                            data = JSON.parse(data);
                            if (data.length > 0) {
                                var str = "";
                                $.each(data, function (key, value) {

                                    var href = "{{route("notif.click")}}?id=" + value['id'];
                                    str +=
                                        '<li class="list-group-item">' +
                                        '<a href="' + href + '" class="user-list-item">' +
                                        '<div class="icon bg-info">' +
                                        '<i class="zmdi zmdi-account"></i>' +
                                        '</div>' +
                                        '<div class="user-desc">' +
                                        '<span class="name">' + value['title'] + '</span>' +
                                        '<span class="desc">' + value['message'] + '</span>' +
                                        '<span class="time">' + value['created_at'] + '</span>' +
                                        '</div>' +
                                        '</a>' +
                                        '</li>';
                                });
                                $('#notif_list').html(str);
                                $('.noti-dot').html('<span class="dot"></span><span class="pulse"></span>');
                                playSound();

                            }
                            else {
                                $('#notif_list').html("");
                                $('.noti-dot').html("");

                            }
                        });
                }, 10000);
                function playSound(){
                    var filename="{{url("backend")}}/notify";
                    document.getElementById("sound").innerHTML='<audio autoplay="autoplay"><source src="' + filename + '.mp3" type="audio/mpeg" /><source src="' + filename + '.ogg" type="audio/ogg" /><embed hidden="true" autostart="true" loop="false" src="' + filename +'.mp3" /></audio>';
                }
            });
        </script>
        @endif
</body>
</html>