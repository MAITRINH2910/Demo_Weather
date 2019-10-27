$(document).ready(function () {
    $("#owl-demo").owlCarousel({

        autoPlay: 3000, //Set AutoPlay to 3 seconds

        items: 6,
        itemsDesktop: [640, 5],
        itemsDesktopSmall: [414, 4]

    });

});


//Local Weather
$(document).ready(function () {

    var x = document.getElementById("demo");
    if (navigator.geolocation) {

        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        x.innerHTML = "Geolocation is not supported by this browser.";
    }

    function showPosition(position) {

        var lat = position.coords.latitude;
        var lon = position.coords.longitude;

        $.ajax({
            url: "/local-weather",
            type: "GET",
            data: {
                lat: lat,
                lon: lon
            },
            success: function (value) {
                var temp = value.main.temp;
                var icon = '<span><img class="weather-widget__img" src="https://openweathermap.org/img/wn/'
                    + value.weather[0].icon + '.png"  width="75" height="75"></span>';
                var city = '<span>' + value.name + '</span>';
                var descript_local = '<span>' + value.weather[0].description + '</span>';
                var tempMin = '<span>' + value.main.temp_min + ' &degC' + '</span>';
                var tempMax = '<span>' + value.main.temp_max + '&degC' + '</span>';
                var pressure = '<span>' + value.main.pressure + 'hpa.' + '</span>';
                var humidity = '<span>' + value.main.humidity + '%' + '</span>';
                var wind = '<span>' + value.wind.speed + 'm/s' + '</span>';

                $('#city').append(city);
                $('#icon_test').append(icon);
                $('#temparature').append(temp);
                $('#descript_local').append(descript_local);
                $('#tempMin_local').append(tempMin);
                $('#tempMax_local').append(tempMax);
                $('#pressure_local').append(pressure);
                $('#humidity_local').append(humidity);
                $('#wind_local').append(wind);

            }
        })
    }
});
//Time-JavaScript

function startTime() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('txt').innerHTML =
        h + ":" + m + ":" + s;
    var t = setTimeout(startTime, 500);
}

function checkTime(i) {
    if (i < 10) {
        i = "0" + i
    }
    ; // add zero in front of numbers < 10
    return i;
}
//Time-JavaScript

//Show more - Show less
if ($('.cityList').length > 1) {
    $('tr[city]:visible').each(function () {
        var city = $(this).attr('city');
        $(this).nextAll('tr[city="' + city + '"]').hide();
    });
}
$(".show-more").click(function () {
    var cityName = $(this).attr("show");
    console.log(cityName);
    $('[city="' + cityName + '"]:gt(0)').toggle();
    $(this).text() === 'Show less' ? $(this).text('Show more') : $(this).text('Show less');
});