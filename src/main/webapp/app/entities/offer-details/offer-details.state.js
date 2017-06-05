(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offer-details', {
            parent: 'entity',
            url: '/offer-details?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.offerDetails.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offer-details/offer-details.html',
                    controller: 'OfferDetailsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('offerDetails');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('offer-details-detail', {
            parent: 'offer-details',
            url: '/offer-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.offerDetails.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offer-details/offer-details-detail.html',
                    controller: 'OfferDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('offerDetails');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OfferDetails', function($stateParams, OfferDetails) {
                    return OfferDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offer-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offer-details-detail.edit', {
            parent: 'offer-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer-details/offer-details-dialog.html',
                    controller: 'OfferDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferDetails', function(OfferDetails) {
                            return OfferDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offer-details.new', {
            parent: 'offer-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer-details/offer-details-dialog.html',
                    controller: 'OfferDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                isValid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offer-details', null, { reload: 'offer-details' });
                }, function() {
                    $state.go('offer-details');
                });
            }]
        })
        .state('offer-details.edit', {
            parent: 'offer-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer-details/offer-details-dialog.html',
                    controller: 'OfferDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferDetails', function(OfferDetails) {
                            return OfferDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offer-details', null, { reload: 'offer-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offer-details.delete', {
            parent: 'offer-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offer-details/offer-details-delete-dialog.html',
                    controller: 'OfferDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfferDetails', function(OfferDetails) {
                            return OfferDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offer-details', null, { reload: 'offer-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
