<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<!-- Chrome, Firefox OS and Opera -->
<meta name="theme-color" content="#353d47">
<!-- Windows Phone -->
<meta name="msapplication-navbutton-color" content="#353d47">
<!-- iOS Safari -->
<meta name="apple-mobile-web-app-status-bar-style" content="#353d47">
<title>JHipster - Generate your Spring Boot + Angular apps!</title>

<!-- CSS Libs -->
<link rel="stylesheet" type="text/css"
	href="/webjars/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="https://jhipster.github.io/lib/css/font-awesome.min.css">

<!-- CSS App -->
<link rel="stylesheet" type="text/css"
	href="https://jhipster.github.io/css/style.css">
<link rel="stylesheet" type="text/css"
	href="https://jhipster.github.io/css/themes/flat-blue.css">

<link rel="stylesheet" type="text/css"
	href="https://jhipster.github.io/css/home.css">

<!-- Bootstrap Docs CSS subset to get icon -->
<!--<link href="/css/bootstrap-docs.css" rel="stylesheet">-->

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

<!-- Favicon with QHD -->
<link rel="icon" type="image/png"
	href="https://jhipster.github.io/images/logo/favicon-32x32.png"
	sizes="32x32" />
<link rel="icon" type="image/png"
	href="https://jhipster.github.io/images/logo/favicon-16x16.png"
	sizes="16x16" />
</head>

<body class="flat-blue">
	<div class="app-container home">
		<div class="row content-container">
			<nav class="navbar navbar-inverse navbar-fixed-top navbar-top">
				<div class="container-fluid">
					<div class="navbar-header">
						<button type="button" class="navbar-expand-toggle">
							<i class="fa fa-bars icon"></i>
						</button>
						<ol class="breadcrumb navbar-breadcrumb">
							<li class="active"></li>
						</ol>
						<button type="button"
							class="navbar-right-expand-toggle pull-right visible-xs">
							<i class="fa fa-th icon"></i>
						</button>
					</div>
					<ul class="nav navbar-nav navbar-right">
						<button type="button"
							class="navbar-right-expand-toggle pull-right visible-xs light">
							<i class="fa fa-times icon"></i>
						</button>
						<li class=""><a
							href="https://github.com/jhipster/generator-jhipster"
							class="dropdown-toggle navlink" target="_blank" role="button"><i
								class="fa fa-github fa-2x"></i></a></li>
					</ul>

				</div>
			</nav>
			<div class="side-menu sidebar-inverse">
				<nav class="navbar navbar-default" role="navigation">
					<div class="side-menu-container">
						<div class="navbar-header">
							<a class="navbar-brand" href="/">
								<div class="icon">
									<img
										src="https://jhipster.github.io/images/logo/logo-jhipster2x.png"
										height="50px">
								</div>
								<div class="title brand">JHipster</div>
							</a>
							<button type="button"
								class="navbar-expand-toggle pull-right visible-xs">
								<i class="fa fa-times icon"></i>
							</button>
						</div>
						<ul class="nav navbar-nav">
							<li><a href="/"> <span class="icon fa fa-home"></span><span
									class="title">Home</span>
							</a></li>
							<li><a href="https://jhipster.github.io/releases/"> <span
									class="icon fa fa-file-text-o"></span><span class="title">Release
										notes</span>
							</a></li>
							<li class="panel panel-default dropdown"><a
								data-toggle="collapse" href="#dropdown-1"> <span
									class="icon fa fa-road"></span><span class="title">JHipster
										in a few minutes</span>
							</a> <!-- Dropdown level 1 -->
								<div id="dropdown-1" class="panel-collapse collapse">
									<div class="panel-body">
										<ul class="nav navbar-nav">
											<li><a href="https://jhipster.github.io/tech-stack/"><i
													class="fa fa-fw fa-stack-overflow"></i> Technology stack</a></li>
											<li><a href="/presentation" target="_blank"><i
													class="fa fa-fw fa-desktop"></i> Official JHipster slides</a></li>
										</ul>
									</div>
								</div></li>
							<li><a href="https://jhipster.github.io/tips/"><span
									class="icon fa fa-fw fa-cogs"></span><span class="title">Tips'n
										tricks</span></a></li>
						</ul>
					</div>
					<!-- /.navbar-collapse -->
				</nav>
			</div>
			<!-- Main Content -->


			<div id="page-wrapper" class="container-fluid">
				<div class="side-body" id="page-top">
					<div class="home-container">
						<!-- about and news section -->
						<section id="about" class="home-section">

							<div class="container">
								<div class="row">
									<div class="col-lg-12 text-center">
										<h2 class="heading">JHipster news and events</h2>
										<hr class="star-primary">
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<b>To get the latest JHipster news, please follow us on
											Twitter: <a href="https://twitter.com/java_hipster"
											target="_blank"><i class="fa fa-twitter"></i>
												@java_hipster</a>
										</b>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div>
											<ul>
												<li><b>April 21, 2017 in Rennes, France:</b> at <a
													href="http://www.breizhcamp.org/" target="_blank">BreizhCamp</a>,
													Pierre Besson will deliver a conference session on JHipster
													4</li>
											</ul>
										</div>
									</div>
								</div>
							</div>

						</section>

					</div>

					<!-- Footer -->
					<footer class="text-center">
						<div class="footer-above">
							<div class="container">
								<div class="row">
									<div class="footer-col col-md-4">
										<div class="col-md-4 col-sm-12">
											<h5 class="text-left">
												<i class="fa fa-coffee fa-fw"></i> <a class="link"
													href="https://jhipster.github.io/team/">Team</a>
											</h5>
											<h5 class="text-left">
												<i class="fa fa-pencil fa-fw"></i> <a class="link"
													href="https://jhipster.github.io/artwork">Artwork</a>
											</h5>
										</div>
										<div class="col-md-4 col-sm-12">
											<h5 class="text-left">
												<i class="fa fa-gavel fa-fw"></i> <a class="link"
													href="https://jhipster.github.io/policies/">Policies</a>
											</h5>
											<h5 class="text-left">
												<i class="fa fa-gift fa-fw"></i> <a class="link"
													href="https://jhipster.github.io/thanks/">Thanks</a>
											</h5>
										</div>
										<div class="col-md-4 col-sm-12">
											<h5 class="text-left">
												<i class="fa fa-archive fa-fw"></i> <a class="link"
													href="https://jhipster.github.io/documentation-archive/">Archives</a>
											</h5>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="footer-below">
							<div class="container">
								<div class="row">
									<div class="col-lg-12">
										Copyright &copy; JHipster 2016 | Theme based on <a
											href="https://github.com/IronSummitMedia/startbootstrap-freelancer"
											target="_blank">Freelancer</a> and <a
											href="https://github.com/tui2tone/flat-admin-bootstrap-templates"
											target="_blank">Flat admin</a>
									</div>
								</div>
							</div>
						</div>
					</footer>
				</div>
				<!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
				<div class="scroll-top page-scroll visible-xs visible-sm">
					<a class="btn btn-primary" href="#page-top"> <i
						class="fa fa-chevron-up"></i>
					</a>
				</div>
			</div>
			<!-- /#page-wrapper -->
		</div>
		<!-- /#wrapper -->
	</div>
	<!-- Javascript Libs -->
	<script type="text/javascript"
		src="/webjars/jquery/jquery.min.js"></script>
	<script type="text/javascript"
		src="/webjars/bootstrap/js/bootstrap.min.js"></script>

	<!--App Javascript -->
	<script type="text/javascript"
		src="https://jhipster.github.io/js/app.js"></script>

</body>

</html>
