<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Transaction</title>
    <style>
        .success {
            background: forestgreen;
            font-size: 30px;
            color: #fff;
            padding: 20px;
            border:1px solid #3c763d;
            border-radius:5px ;
        }
        .error{
            background: orangered;
            font-size: 30px;
            color: #fff;
            padding: 20px;
            border:1px solid #78341a;
            border-radius:5px ;
        }

    </style>
</head>
<body>
<div>
    @if($status=="1")
        <p class="success">عملیات با موفقیت انجام شد با برگشت به برنامه می توانید به فایل صوتی کامل دسترسی داشته باشید</p>
        <p class="success">شماره رهگیری تراکنش:</p>
        <p class="success">{{$ref}}</p>
    @elseif($status=="2")
        <p class="error">عملیات به مشکل برخورد کرد</p>
    @elseif($status=="3")
        <p class="error">عملیات به مشکل برخورد کرد</p>
    @endif
</div>
</body>
</html>