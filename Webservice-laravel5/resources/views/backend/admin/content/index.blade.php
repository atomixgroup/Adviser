@extends("backend.master")

@section("menu")
    @include("backend.admin.side_menu")

@endsection
@section("content")
    <script>

    </script>
    <div class="row">
        <div class="col-sm-12">
            <div class="card-box">

                @if(isset($data->contents))
                    <div class="table-rep-plugin">
                        <div class="table-responsive" data-pattern="priority-columns">
                            <table id="tech-companies-1" class="table  table-striped">
                                <thead>
                                <tr>
                                    <th>شناسه</th>
                                    <th>تیتر مطلب</th>
                                    <th >وضعیت</th>
                                    <th>عملیات</th>
                                </tr>
                                </thead>
                                <tbody>
                                @foreach($data->contents as $content)
                                    <tr>
                                        <td>{{$content['id']}}</td>
                                        <td>{{$content->title}}</td>
                                        <td>{{$content->status}}</td>
                                        <td>
                                            <a href="{{route("content.register",["id"=>$content['id']])}}"><i
                                                        class="zmdi zmdi-edit zmdi-hc-2x"></i></a> &nbsp;&nbsp;<a
                                                    class="delete-icon" data-id="{{$content['id']}}"><i class="zmdi zmdi-delete zmdi-hc-2x"></i></a>
                                        </td>
                                    </tr>
                                @endforeach

                                </tbody>
                            </table>
                        </div>

                    </div>
                @endif
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="confirm" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">عملیات تایید</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    آیا برای این عملیات اطمینان دارید ؟
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">نه</button>
                    <button id="yes-button" type="button" class="btn btn-primary">بله</button>
                </div>
            </div>
        </div>
    </div>

@endsection
@section("js")
    <script src="{{url("backend/js")}}/rwd-table.min.js"></script>

    <script>


        var id = -1;
        $(function () {

            $(".delete-icon").click(function () {
                id = $(this).data("id");
                $('#confirm').modal('show')
            });
            $("#yes-button").click(function () {
                $('#confirm').modal('hide');

                window.location.href = "{{route("content.delete")}}?id=" + id;
            });

        });

    </script>

@endsection
@section("css")
    <link href="{{url("backend/style")}}/dropify.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="{{url("backend/style")}}/rwd-table.min.css">
    <style>
        .pass-show:hover, .delete-icon:hover {
            cursor: pointer;
        }
    </style>
@endsection
