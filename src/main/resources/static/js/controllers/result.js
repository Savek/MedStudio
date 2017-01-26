
angular
    .module('admin')
    .config(['ChartJsProvider', function (ChartJsProvider) {
        // Configure all charts
        ChartJsProvider.setOptions({
            chartColors: ['#58a554'],
        });
    }])
    .controller("resultController", function ($scope, $http, $location, $routeParams) {

        $scope.onClick = function (day) {
            var date = new Date(day);
            date.setDate(date.getDate() + 1);
            $location.path( "/resultDetails/" + $routeParams.userId + "/" + $routeParams.resultType + "/" + date.toISOString() );
        };

        $scope.dt = new Date();

        $scope.inlineOptions = {
            minDate: new Date(),
            showWeeks: true
        };

        $scope.dateOptions = {
            formatYear: 'yyyy',
            startingDay: 1,
            minMode: 'month'
        };

        $scope.toggleMin = function() {
            $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
            $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
        };

        $scope.toggleMin();

        $scope.open2 = function() {
            $scope.popup2.opened = true;
        };

        $scope.chart = function() {

            $http.get('/getResultsMonthAndYear/'+ $routeParams.userId + '/' + $routeParams.resultType, {params:{"date": $scope.dt}})
                .then(function (response) {
                    $scope.days = response.data;
                });
        };
        $scope.chart();

        $scope.setDate = function(year, month, day) {
            $scope.dt = new Date(year, month, day);
        };

        $scope.format = 'MM-yyyy';

        $scope.popup2 = {
            opened: false
        };

        var tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        var afterTomorrow = new Date();
        afterTomorrow.setDate(tomorrow.getDate() + 1);
        $scope.events = [
            {
                date: tomorrow,
                status: 'full'
            },
            {
                date: afterTomorrow,
                status: 'partially'
            }
        ];
    });