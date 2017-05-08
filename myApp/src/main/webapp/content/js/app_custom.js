var $BODY = $('body'), $MENU_TOGGLE = $('#menu_toggle');

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
