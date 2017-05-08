var CURRENT_URL = window.location.href.split('#')[0].split('?')[0], $BODY = $('body'), $MENU_TOGGLE = $('#menu_toggle'), $SIDEBAR_MENU = $('#sidebar-menu'), $SIDEBAR_FOOTER = $('.sidebar-footer'), $LEFT_COL = $('.left_col'), $RIGHT_COL = $('.right_col'), $NAV_MENU = $('.nav_menu'), $FOOTER = $('footer');

// toggle small or large menu
$MENU_TOGGLE.on('click', function() {
	console.log('clicked - menu toggle');
	if ($BODY.hasClass('nav-md')) {
		$MENU_TOGGLE.find('i').addClass('fa-rotate-90');
	} else {
		$MENU_TOGGLE.find('i').removeClass('fa-rotate-90');
	}
	$BODY.toggleClass('nav-md nav-sm');
});
