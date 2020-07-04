<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Benelli</title>
	</head>
	<body>
		<font color="blue">你好，${xsession.user.name?if_exists}同学！${pathVal}， ${param}， Attr: ${attr?if_exists}</font>
	</body>
</html>