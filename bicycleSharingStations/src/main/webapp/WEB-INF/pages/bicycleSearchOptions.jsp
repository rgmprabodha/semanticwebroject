<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
<script type="text/javascript" src ="webjars/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.20/datatables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/responsive/2.2.3/js/dataTables.responsive.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/responsive/2.2.3/js/responsive.bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script type="text/javascript">
   var bicycleStationsTable;
   $(function () {
        $("#bicycleStations").hide();
        $ ("#search-form").submit(function(event){
           // stop submit the form, we will post it manually.
            event.preventDefault();
            $(".error").remove();
            if($("#city").val() != 0){
                searchStationsByCity();
            }else{
                $('#city').after('<span class="error">This field is required</span>');
            }
        });
    });

    function searchStationsByCity () {
        $("#bicycleStations").show();
        //$('#loadingModal').modal('show');
        var city = $("#city").val();
        $("#btn-search").prop("disabled", true);
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/search/" + city,
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {
                setTableRows(data);
                $("#btn-search").prop("disabled", false);
                //$('#loadingModal').modal('hide');
            },
            error: function (e) {
                var json = "<h4>Ajax Response</h4><pre>"+ e.responseText + "</pre>";
                $('#error-msg').innerHTML=json;
                console.log("ERROR : ", e);
                $("#btn-search").prop("disabled", false);
            }
        });
    }

    // TODO take RDFa values from the data store, dynamically
    // not hardcoding.
    // TODO check value.ID is not working.
    // TODO add content tag for date field
    // TODO xsd date time for lat, long
    function setTableRows(data){
        var res='';
        $.each (data, function (key, value) {
        console.log(value.ID);
            res +=
            '<tr>'+
                '<td vocab="https://opendata.paris.fr/api/records/1.0/search/?dataset=velib-emplacement-des-stations" resource="'+value.ID+'" typeof="PublicBicycleStation"><span property="stationName">'+value.name+'</span></td>'+
                '<td prefix="geo: https://www.w3.org/2003/01/geo/wgs84_pos#" resource="'+value.ID+'"><span property="lat">'+value.lat+'</span></td>'+
                '<td prefix="geo: https://www.w3.org/2003/01/geo/wgs84_pos#" resource="'+value.ID+'"><span property="lon">'+value.lon+'</span></td>'+
                '<td prefix="onto: http://www.semanticweb.org/emse/ontologies/2019/11/bicycle_stations.owl#" resource="'+value.ID+'"><span property="capacity">'+value.capacity+'</span></td>'+
                '<td prefix="onto: http://www.semanticweb.org/emse/ontologies/2019/11/bicycle_stations.owl#" resource="'+value.ID+'"><span property="availableBikes">'+value.availableBikes+'</span></td>'+
                '<td prefix="onto: http://www.semanticweb.org/emse/ontologies/2019/11/bicycle_stations.owl#" resource="'+value.ID+'"><span property="updatedDatetime">'+value.localUpdateDateTime+'</span></td>'+
                '<td>'
            '</tr>';
        });
        $('tbody').html(res);
    }
</script>

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.20/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.bootstrap.min.css"/>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<style type="text/css">
.table .thead-dark th {
    color: #007bff;
    background-color: #343a40;
    }

.error {
  color: red;
  margin-left: 5px;
}

label.error {
  display: inline;
}
#footer{
    background: #007bff;
}
.bg-light{

}
</style>


</head>
<body>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><h1>Bicycle Sharing Stations</h1></a>
        </div>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item">
                <a class="nav-link" href="http://localhost:8080/">Home</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">Search more <span class="sr-only">(current)</span></a>
              </li>
            </ul>
          </div>
        </nav>
    </div>
</nav>

<div class="container">
    <div class="starter-template">
        <section id="error-msg" class="error"></section>
        <div class="card">
            <div class="card-body">
                <form class = "form-horizontal" id="search-form">
                        <div class="form-group row">
                            <label for="fromDate" class="col-sm-2 control-label">From Date</label>
                            <div class="col-sm-4">
                                <input type="date" class="form-control" id="fromDate" name="fromDate"
                                       autocomplete="off">
                            </div>
                            <label for="toDate" class="col-sm-2 control-label">To Date</label>
                            <div class="col-sm-4">
                                <input type="date" class="form-control" id="toDate" name="toDate"
                                       autocomplete="off">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="fromTime" class="col-sm-2 control-label">From Date</label>
                            <div class="col-sm-4">
                                <input  type="time"  min="00:00" max="24:00"  class="form-control"  id="fromTime" name="fromTime"
                                       autocomplete="off">
                            </div>
                            <label for="toTime" class="col-sm-2 control-label">To Date</label>
                            <div class="col-sm-4">
                                <input type="time" min="00:00" max="24:00"  class="form-control" id="toTime" name="toTime"
                                       autocomplete="off">
                            </div>
                        </div>

                    <div class="form-group row">
                        <label for="city" class="col-sm-2 control-label">Select a City<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-7">
                            <select class="form-control" id="city" name="city">
                                <option value="0">Select a City</option>
                                <option value="SAINT-ETIENNE">SAINT-ETIENNE</option>
                                <option value="LYON">LYON</option>
                                <option value="TOULOUSE">TOULOUSE</option>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <button id="btn-search" type="submit" class="btn btn-primary float-right">Search</button>
                        </div>
                    </div>
                </form>
                <br/>
                <div class="row" id="bicycleStations">
                    <table id="bicycleStationsTable" class="table table-striped table-bordered dt-responsive nowrap">
                        <thead>
                        <tr>
                            <th>Station Name</th>
                            <th>Latitude</th>
                            <th>Longitude</th>
                            <th>Capacity</th>
                            <th>Available</th>
                            <th>More</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div
    </div>
</div>
<div class = "container">
    <footer class="page-footer font-small blue fixed-bottom">
        <div id="footer" class="footer-copyright text-center py-3">© 2020 Copyright: Semantic Web
        </div>
    </footer>
</div>

<div class="modal fade" id="loadingModal" tabindex="-1" role="dialog" data-backdrop="static" area-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="loader"></div>
            </div>
        </div>
    </div>
</div>

</body>

</html>
