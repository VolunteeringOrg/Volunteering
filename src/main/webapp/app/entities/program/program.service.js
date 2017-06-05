(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Program', Program);

    Program.$inject = ['$resource', 'DateUtils'];

    function Program ($resource, DateUtils) {
        var resourceUrl =  'api/programs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateTo = DateUtils.convertDateTimeFromServer(data.dateTo);
                        data.dateFrom = DateUtils.convertDateTimeFromServer(data.dateFrom);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
