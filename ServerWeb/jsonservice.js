/*
Encapsula requisições JSON.
By Fábio Dias (fabiodcs@gmail.com)
*/

var JSONService = {
    Request: function (url, params, callback_success, callback_error, type)
    {
        params = (typeof params == 'object') ? JSON.stringify(params) : params;
        var settings = {
            url: url,
            data: params,
            contentType: "application/json; charset=utf-8",
            cache: false,
            type: type,
            crossDomain: true,
            dataType: 'json'
        };
        var ajaxRequest = $.ajax(settings)
        .done(function apiCallback(data)
        {
            callback_success(data);
        })
        .fail(function apiOnError(jqXHR, textStatus, errorThrown)
        {
            if (callback_error != undefined)
            {
                callback_error(jqXHR, textStatus, errorThrown, settings);
            }
        })

        return ajaxRequest;
    },
    Get: function (url, params, callback_success, callback_error)
    {
        var ajaxRequest = this.Request(url, params, callback_success, callback_error, "GET");
        return ajaxRequest;
    },
    Post: function (url, params, callback_success, callback_error)
    {
        var ajaxRequest = this.Request(url, params, callback_success, callback_error, "POST");
        return ajaxRequest;
    },
    Put: function (url, params, callback_success, callback_error)
    {
        var ajaxRequest = this.Request(url, params, callback_success, callback_error, "PUT");
        return ajaxRequest;
    },
    setLoading: function (namespace, callback_start, callback_stop)
    {
        if ((callback_start != null) & (callback_stop != null))
        {
            $(document).bind("ajaxStart." + namespace, function ()
            {
                callback_start();
            });

            $(document).bind("ajaxStop." + namespace, function ()
            {
                callback_stop();
                $(document).unbind("." + namespace);
            });
        }
    },
    setLoadingButton: function (namespace, button, callback_start, callback_stop)
    {
        if ((callback_start != null) & (callback_stop != null))
        {
            $(document).bind("ajaxStart." + namespace, function ()
            {
                callback_start(button);
            });
            $(document).bind("ajaxStop." + namespace, function ()
            {
                callback_stop(button);
                $(document).unbind("." + namespace);
            });
        }
    }
};