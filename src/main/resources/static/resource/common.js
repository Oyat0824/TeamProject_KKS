$('select[data-value]').each(function(idx, element) {
	const el = $(element);
	const defaultValue = el.attr('data-value').trim();

	if (defaultValue.length > 0) {
		el.val(defaultValue);
	}
})


$(".close-btn").click(function() {
	$(".layer-bg").fadeOut();
	$(".layer").fadeOut();
})

$(".modal-exam").click(function() {
	$(".layer-bg").fadeIn();
	$(".layer").fadeIn();
})

$(".layer-bg").click(function() {
	$(".layer-bg").fadeOut();
	$(".layer").fadeOut();
})

$(".toggle-btn").click(function() {
	$(this).toggleClass("active");
})

$(".close").click(function(){
	$(".layer-bg").fadeOut();
	$(".layer").fadeOut();
})