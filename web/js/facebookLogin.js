// This is called with the results from from FB.getLoginStatus().
function statusChangeCallback(action, response) {
    console.log('statusChangeCallback');
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
        // Logged into your app and Facebook.
        testAPI(action);
    } else {
        // The person is not logged into your app or we are unable to tell and isn't on the login view.
        if (window.location.href.indexOf("https://localhost:8443/jsp/login.jsp") == -1) {
            window.location.replace("https://localhost:8443/jsp/login.jsp");
        }

    }
}

// This function is called when someone finishes with the Login
// Button.  See the onlogin handler attached to it in the sample
// code below.
function checkLoginState(action) {
    FB.getLoginStatus(function(action, response) {
        statusChangeCallback(action, response);
    });
}

window.fbAsyncInit = function() {
    FB.init({
        appId      : '193768198092565',
        cookie     : true,  // enable cookies to allow the server to access
                            // the session
        xfbml      : true,  // parse social plugins on this page
        version    : 'v2.12' // use graph api version 2.8
    });

    // Now that we've initialized the JavaScript SDK, we call
    // FB.getLoginStatus().  This function gets the state of the
    // person visiting this page and can return one of three states to
    // the callback you provide.  They can be:
    //
    // 1. Logged into your app ('connected')
    // 2. Logged into Facebook, but not your app ('not_authorized')
    // 3. Not logged into Facebook and can't tell if they are logged into
    //    your app or not.
    //
    // These three cases are handled in the callback function.

    //FB.getLoginStatus(function(response) {
    //    statusChangeCallback(response);
    //});

};

// Load the SDK asynchronously
(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

// Here we run a very simple test of the Graph API after login is
// successful.  See statusChangeCallback() for when this call is made.
function testAPI(action) {
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', {fields: 'first_name, last_name, email'}, function(response) {
        console.log(response);
        console.log('Successful login for: ' + response.name + ' ' + response.email);
        //document.getElementById('status').innerHTML =
        //    'Thanks for logging in, ' + response.name + '!';
        if (action === 'login') {
            window.location = "https://localhost:8443/RegistroServlet.do?facebook=yes&nick=" + response.id + "&email=" + response.email + "&name=" + response.first_name + "&lastName=" + response.last_name;
        } else if (action === 'register') {
            window.location = "https://localhost:8443/RegistroServlet.do?facebook=yes&nick=" + response.id + "&email=" + response.email + "&name=" + response.first_name + "&lastName=" + response.last_name;
        } else {

        }

    });
}