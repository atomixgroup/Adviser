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
                            <h4 class="m-b-30 m-t-0 header-title"><b>اضافه و ویرایش مطلب</b></h4>


                          

                            <div id="status-drop-down" class="form-group">
                                <label class="col-sm-5 control-label">وضعیت</label>
                                <div class="col-sm-7">
                                    <a href="#" id="inline-status" data-type="select" data-pk="2"
                                       data-value="{{isset($data->content)?$data->content->status:""}}"
                                       data-title="انتخاب وضعیت"></a>
                                </div>
                                <input type="hidden" id="status" name="status"
                                       value="{{isset($data->content)?$data->content->status:""}}">

                            </div>
                            <script>
                                var statusSource = [ {"value":"active","text":"فعال"},{"value":"deactivate","text":"غیرفعال"}];
                                var status="";
                                $(function (e) {
                                    $('#inline-status').editable({

                                        mode: 'inline',
                                        inputclass: 'input-medium privacy-select-status',
                                        source: function () {
                                            return statusSource;
                                        },
                                        success: function (data) {
                                            $("#status").val(status);

                                        }
                                    });
                                    $('#status-drop-down').on('change', ".privacy-select-status", function () {
                                        status = $('.privacy-select-status').val();
                                    }).blur();
                                });

                            </script>
                            <div id="cat-drop-down" class="form-group">
                                <label class="col-sm-5 control-label">دسته بندی</label>
                                <div class="col-sm-7">
                                    <a href="#" id="inline-cat" data-type="select" data-pk="2"
                                       data-value="{{isset($data->content)?$data->content->g_id:""}}"
                                       data-title="انتخاب دسته بندی"></a>
                                </div>
                                <input type="hidden" id="cat" name="g_id"
                                       value="{{isset($data->content)?$data->content->g_id:""}}">

                            </div>
                            <script>
                                    <?php $str = "{value:-1,text:'انتخاب نشده'},"; ?>
                                        @foreach($data->categories as $category)
                                    <?php $str .= "{value:" . $category['id'] . ", text:'" . $category->title. "'},"; ?>
                                        @endforeach
                                var catSource = [<?php echo substr($str, 0, strlen($str) - 1);?>];
                                var cat="";
                                $(function (e) {
                                    $('#inline-cat').editable({

                                        mode: 'inline',
                                        inputclass: 'input-medium privacy-select-cat',
                                        source: function () {
                                            return catSource;
                                        },
                                        success: function (data) {
                                            $("#cat").val(cat);
                                        }
                                    });
                                    $('#cat-drop-down').on('change', ".privacy-select-cat", function () {
                                        cat = $('.privacy-select-cat').val();
                                    }).blur();
                                });

                            </script>
                            <div class="form-group">
                                <label class="col-sm-5 control-label">تیتر مطلب:*</label>
                                <div class="col-sm-7">
                                    <a href="#" id="inline-title" data-type="text" data-pk="1"
                                       data-title="تیتر را وارد کنید">{{isset($data->content)?$data->content->title:""}}</a>
                                </div>
                                <input type="hidden" id="title" name="title"
                                       value="{{isset($data->content)?$data->content->title:""}}">
                            </div>
                            <div class="form-group">
                                <label class="col-sm-5 control-label">توضیحات:*</label>
                                <div class="col-sm-7">
                                    <a href="#" id="inline-description" data-type="textarea" data-pk="1"
                                       data-title="توضیحات را وارد کنید">{{isset($data->content)?$data->content->body:""}}</a>
                                </div>
                                <input type="hidden" id="description" name="body"
                                       value="{{isset($data->content)?$data->content->body:""}}">

                            </div>
                            <div class="form-group">
                                <label class="col-sm-5 control-label">قیمت فروش:*</label>
                                <div class="col-sm-7">
                                    <a href="#" id="inline-price" data-type="text" data-pk="1"
                                       data-title="قیمت را وارد کنید">{{isset($data->content)?$data->content->price:""}}</a>
                                </div>
                                <input type="hidden" id="price" name="price"
                                       value="{{isset($data->content)?$data->content->price:""}}">
                            </div>



                        </div><!-- end col -->
                        <div class="col-lg-6">
                            <div class="row">
                                <div class="card-box">
                                    <h4 class="header-title m-t-0 m-b-30">آپلود فایل صوتی 1</h4>
                                    <input type="file" name="file1" class="dropify"  />
                                </div>
                                <div class="card-box">
                                    <h4 class="header-title m-t-0 m-b-30">آپلود فایل صوتی 2</h4>
                                    <input type="file" name="file2" class="dropify"  />
                                </div>
                                <div class="card-box">
                                    <h4 class="header-title m-t-0 m-b-30">آپلود فایل صوتی 3</h4>
                                    <input type="file" name="file3" class="dropify"  />
                                </div>
                                <div class="card-box">
                                    <h4 class="header-title m-t-0 m-b-30">آپلود فایل صوتی 4</h4>
                                    <input type="file" name="file4" class="dropify"  />
                                </div>
                                <div class="card-box">
                                    <h4 class="header-title m-t-0 m-b-30">آپلود فایل صوتی 5</h4>
                                    <input type="file" name="file5" class="dropify"  />
                                </div>
                            </div>

                        </div>


                    </div><!-- end row -->
                </div>
                <input name="submit" value="ذخیره" type="submit">
            </div><!-- end col -->
        </div>
        <!-- end row -->


    </form>
@endsection
@section("js")
    <script src="{{url("backend/js")}}/dropify.min.js"></script>
    <script>
        var state = 0;
        var gender = 0;
        var city = 0;
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
            $('#inline-description').editable({
                type: 'text',
                mode: 'inline',
                success: function (data) {
                    setTimeout(function () {
                        $("#description").val($('#inline-description').text());
                    }, 500);

                }
            });
            $('#inline-price').editable({
                type: 'text',
                mode: 'inline',
                success: function (data) {
                    setTimeout(function () {
                        $("#price").val($('#inline-price').text());
                    }, 500);

                }
            });

        });
    </script>
    <script src="{{url("backend/js")}}/moment.js"></script>
    <script type="text/javascript" src="{{url("backend/js")}}/bootstrap-editable.min.js"></script>
    <script type="text/javascript" src="{{url("backend/js")}}/jquery.xeditable.js"></script>
    <script type="text/javascript">
        $('.dropify').dropify({
            messages: {
                'default': 'یک فایل را به اینجا بکشید و رها کنید یا اینجا کلیک کنید',
                'replace': 'برای جایگزینی یک فایل را بکشید و رها کنید یا اینجا کلیک کنید',
                'remove': 'پاک کردن',
                'error': 'خطایی رخ داده است.'
            },
            error: {
                'fileSize': 'اندازه فایل بزرگ است. (حداکثر 30 مگابایت)'
            }
        });
    </script>
@endsection
@section("css")
    <link type="text/css" href="{{url("backend/style")}}/bootstrap-editable.css" rel="stylesheet">
    <link href="{{url("backend/style")}}/dropify.min.css" rel="stylesheet" type="text/css"/>
    <link href="{{url("backend/style")}}/custom.css" rel="stylesheet" type="text/css"/>

@endsection