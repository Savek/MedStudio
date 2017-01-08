
angular
    .module('admin')
    .config(['ChartJsProvider', function (ChartJsProvider) {
        // Configure all charts
        ChartJsProvider.setOptions({
            chartColors: ['#58a554'],
        });
    }])
    .controller("LineCtrl", function ($scope, $http, $location, $routeParams) {

        $scope.onClick = function (points, evt) {
            var dateTemp = $scope.dt;
            dateTemp.setDate($scope.labels[points[0]._index]);
            var dateParam = dateTemp.getFullYear() + "-" + (dateTemp.getMonth() + 1) + "-" + dateTemp.getDate();
            $location.path( "/resultDetails/" + $routeParams.userId + "/" + $routeParams.resultType + "/" + dateParam );
            $scope.$apply();
        };
        $scope.datasetOverride = [{yAxisID: 'y-axis-1'}];

        $scope.options = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    },
                ]
            }
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

        $scope.colors = ['#58a554'];
        $scope.chart = function() {

            $scope.labels = [];
            $scope.data = [];
            $scope.dataTemp = [];
            $http.get('/getResultsMonthAndYear/'+ $routeParams.userId + '/' + $routeParams.resultType, {params:{"date": $scope.dt}})
                .then(function (response) {
                    $scope.dataTemp = [];
                    angular.forEach(response.data, function(value, key) {
                        var dateTemp = new Date(key);
                        $scope.labels.push(dateTemp.getDate());
                        $scope.dataTemp.push(value);
                    });
                    $scope.data.push($scope.dataTemp);
                }, function () {

                });
        };
        $scope.chart();

        $scope.setDate = function(year, month, day) {
            $scope.dt = new Date(year, month, day);

        };

        $scope.format = 'MMMM-yyyy';

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