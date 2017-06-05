(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Offer', Offer);

    Offer.$inject = ['$resource', 'DateUtils'];

    function Offer ($resource, DateUtils) {
        var resourceUrl =  'api/offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateFrom = DateUtils.convertDateTimeFromServer(data.dateFrom);
                        data.dateTo = DateUtils.convertDateTimeFromServer(data.dateTo);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
