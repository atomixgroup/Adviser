<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<form action="{{route("form")}}" method="post">
    <label>title:</label>
    <input type="text" name="title" ><br>

    <label>shomare bargh:</label>
    <input type="text" name="g_id" value="@if(isset($g_id)) {{$g_id}} @endif" ><br>

    <label>section:</label>
    <input type="text" name="section" value="@if(isset($section)) {{$section}} @endif"><br>

    <label>body:</label>
    <textarea name="body" rows="10" cols="20"></textarea><br>
    {{ csrf_field() }}
    <input type="submit" value="ثبت">
</form>
</body>
</html>