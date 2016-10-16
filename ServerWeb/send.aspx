<%@ Page Language="C#" AutoEventWireup="true" %>

<%@ Import Namespace="RestSharp" %>
<script runat="server">
    protected void Page_Load(object sender, EventArgs e)
    {
        var client = new RestClient("http://api.hackathon.konkerlabs.net/pub/maeia7o25sa5/Konker");
        var request = new RestRequest(Method.POST);
        request.AddHeader("postman-token", "5fe01098-fd21-23c8-a9f8-77840b7bed63");
        request.AddHeader("cache-control", "no-cache");
        request.AddHeader("content-type", "application/json");
        request.AddHeader("authorization", "Basic bWFlaWE3bzI1c2E1OmVKVDZJN0thb2FLNw==");
        request.AddParameter("application/json", "{\n\t\n\t\"status\": \"2\"\n}", ParameterType.RequestBody);
        IRestResponse response = client.Execute(request);

        var objRetorno = new { status = "2 " };

        string json = new System.Web.Script.Serialization.JavaScriptSerializer().Serialize(objRetorno);
        Response.Write(json);
    }
</script>
