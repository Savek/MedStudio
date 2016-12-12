
angular
        .module(
        'main', [ 'ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap']
        ).config(
            function($routeProvider, $httpProvider) {

                $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

                $routeProvider.when('/', {
                    templateUrl: 'home.html',
                    controller: 'navigation',
                    controllerAs: 'controller'
                }).when('/login', {
                    templateUrl: 'login.html',
                    controller: 'navigation',
                    controllerAs: 'controller'
                }).when('/admin', {
                    templateUrl: 'admin.html',
                    controller: 'navigation',
                    controllerAs: 'controller'
                }).otherwise('/');
            }
        );

