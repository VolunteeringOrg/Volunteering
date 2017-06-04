(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Requirement', Requirement);

    Requirement.$inject = ['$resource'];

    function Requirement ($resource) {
        var resourceUrl =  'api/requirements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
