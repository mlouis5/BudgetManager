$(document).ready(function () {
    'use strict';
    
    function init() {
        $("#resourceContent div").hide(); // Initially hide all content
        $("#resourceTabs li:first").attr("id", "current"); // Activate first tab
        $("#resourceContent div:first").fadeIn(); // Show first tab content
        $('#resourceTabs a').click(function (e) {
            e.preventDefault();
            $("#resourceContent div").hide(); //Hide all content
            $("#resourceTabs li").attr("id", ""); //Reset id's
            $(this).parent().attr("id", "current"); // Activate this
            $('#' + $(this).attr('title')).fadeIn(); // Show content for current tab
        });



        $("#collectiveContent div").hide(); // Initially hide all content
        $("#collectiveTabs li:first").attr("id", "current"); // Activate first tab
        $("#collectiveContent div:first").fadeIn(); // Show first tab content
        $('#collectiveTabs a').click(function (e) {
            e.preventDefault();
            $("#collectiveContent div").hide(); //Hide all content
            $("#collectiveTabs li").attr("id", ""); //Reset id's
            $(this).parent().attr("id", "current"); // Activate this
            $('#' + $(this).attr('title')).fadeIn(); // Show content for current tab
        });
    }
    
    init();
});