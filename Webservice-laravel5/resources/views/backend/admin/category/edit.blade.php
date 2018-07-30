@extends("backend.master")

@section("menu")
    @include("backend.admin.side_menu")
@endsection
@section("content")
    <form enctype="multipart/form-data" method="post" class="form-horizontal">
        {{csrf_field()}}
        <div class="row">
            <div class="col-sm-12">
                <div class="card-box">
                    <div class="row">
                        <div class="col-lg-6">
                            <h4 class="m-b-30 m-t-0 header-title"><b>اضافه و ویرایش دوره</b></h4>

                            <div class="form-group">
                                <label class="col-sm-5 control-label">تیتر دوره:*</label>
                                <div class="col-sm-7">
                                    <a href="#" id="inline-title" data-type="text" data-pk="1"
                                       data-title="تیتر را وارد کنید">{{isset($data->category)?$data->category->title:""}}</a>
                                </div>
                                <input type="hidden" id="title" name="title"
                                       value="{{isset($data->category)?$data->category->title:""}}">
                            </div>

                        </div><!-- end col -->

                    </div><!-- end row -->

                </div>
                <input name="submit" value="ذخیره" type="submit">
            </div><!-- end col -->
        </div>
        <!-- end row -->


    </form>
@endsection
@section("js")
    <script>
        $(function (e) {
            $('#inline-title').editable({
                type: 'text',
                mode: 'inline',
                success: function (data) {
                    setTimeout(function () {
                        $("#title").val($('#inline-title').text());
                    }, 500);

                }
            });
        });
    </script>
    <script src="{{url("backend/js")}}/moment.js"></script>
    <script type="text/javascript" src="{{url("backend/js")}}/bootstrap-editable.min.js"></script>
    <script type="text/javascript" src="{{url("backend/js")}}/jquery.xeditable.js"></script>

@endsection
@section("css")

    <link type="text/css" href="{{url("backend/style")}}/bootstrap-editable.css" rel="stylesheet">

    <link href="{{url("backend/style")}}/custom.css" rel="stylesheet" type="text/css"/>

@endsection