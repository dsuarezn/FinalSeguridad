'use strict';

/**
 * @ngdoc function
 * @name SeguridadApp.controller:SecController
 * @description
 * # SecController
 * Controller of the SeguridadApp
 */
angular.module('SeguridadApp')
  .controller('SecController',function ($scope, $http) {
	
	$scope.port = 7891;
/*$scope.port = 8443;*/
	$scope.mensaje = '';
  	$scope.servidor = 'http://localhost:'+$scope.port;
  	$scope.msg = '';
  	$scope.listaLocal = [];
  	$scope.listaExterna = [];

   	$scope.rutaLocalUpload = '';
   	$scope.rutaExternaRecieve = '';

   	$scope.isAliveExtern=false;

   	$scope.nivel_success="alert-success";
	$scope.nivel_info="alert-info";
	$scope.nivel_warn="alert-warning";
	$scope.nivel_error="alert-danger";


	$scope.mensajeServidorVivo="El servidor esta activo";
	$scope.mensajeServidorNoVivo="El servidor solicitado no esta activo";
	$scope.mensajeServidorVacio="Debe ingresar la ruta de un servidor activo";

	$scope.consultarLocales = function(){


	$http.get('http://localhost:'+$scope.port+'/listUploadFiles')
		 .then(function(success) {
		 		$scope.listaLocal =success.data;
	        },
	        function(error) {
	            console.log('Error: ' + error);
	        });	        
    };

    $scope.consultarRutaLocal = function(){
		$http.get('http://localhost:'+$scope.port+'/listUploadPath')
		 .then(function(success) {
		 		$scope.rutaLocalUpload=success.data.path;
	        },
	        function(error) {
	            console.log('Error: ' + error);
	        });	        
    };

    $scope.consultarRutaExterna = function(){    	
    		$http.get($scope.servidor+'/listRecievedPath')
		 .then(function(success) {
		 		$scope.rutaExternaRecieve =success.data.path;
	        },
	        function(error) {
	            console.log('Error: ' + error);
	        });	    				      
    };

    $scope.consultarArchivosExternos = function(){
	$http.get($scope.servidor+'/listRecievedFiles')
		 .then(function(success) {
		 		$scope.listaExterna =success.data;
	        },
	        function(error) {
	            console.log('Error: ' + error);
	        });	        
    };

    $scope.isAlivePathExterno = function(){
    $scope.isAliveExtern=false;
	$http.get($scope.servidor+'/isAlive')
		 .then(function(success) {
		 		$scope.isAliveExtern=success.data;
		 		 if($scope.isAliveExtern==true){
		 			$scope.mostrarMensaje($scope.mensajeServidorVivo, $scope.nivel_success);
		 			$scope.consultarRutaExterna();
					$scope.consultarArchivosExternos();	
		 		}		 		
	        },
	        function(error) {
	            console.log('Error: ' + error);
	            if($scope.isAliveExtern==false){
		 			$scope.mostrarMensaje($scope.mensajeServidorNoVivo, $scope.nivel_error);
		 		}
	        });	 
	        return $scope.isAliveExtern;     
    };


	$scope.consultarExternos = function(){
		if($scope.servidor!=null &&  $scope.servidor!=''){
			$scope.isAlivePathExterno();					
		}
		else{
			$scope.mostrarMensaje($scope.mensajeServidorVacio, $scope.nivel_warn);
		}
	};

	$scope.consultarLocales();
	$scope.consultarRutaLocal();
	

	$scope.mostrarMensaje = function(mensaje, nivel){	
		$scope.mensaje=mensaje;
		var target = document.getElementById('msg');
		angular.element(target).removeClass("hiden");
		angular.element(target).addClass(nivel);		
		
	};


 	$scope.enviarArchivo = function(file){

		var config = {
		    params: {
		        servidor: $scope.servidor,
		        accion: 'enviar',
		        archivo: file.filename
		    }
		}

		$http.get('http://localhost:'+$scope.port+'/sendFile',config)
	        .then(function(success) {
	            $scope.mensaje = success.data.mensaje;
	            console.log(success);
	            $scope.consultarArchivosExternos();	
	        },
	        function(error) {
	            console.log('Error: ' + error);
	            $scope.consultarArchivosExternos();	
	        });
    };

    $scope.eliminarArchivo = function(file){

		var config = {
		    params: {
		        servidor: $scope.servidor,
		        accion: 'eliminar',
		        archivo: file.filename
		    }
		}

		$http.get('http://localhost:'+$scope.port+'/deleteFile',config)
	        .then(function(success) {
	            $scope.mensaje = success.data.mensaje;
	            console.log(success);
	            $scope.consultarLocales();	
	        },
	        function(error) {
	            console.log('Error: ' + error);
	            $scope.consultarLocales();	
	        });
    };


			

  });
