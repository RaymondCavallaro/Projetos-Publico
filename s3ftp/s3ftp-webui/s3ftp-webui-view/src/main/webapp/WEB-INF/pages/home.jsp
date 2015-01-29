<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:url value="${template }/home/carregar" var="formCarregar" />

<spring:url value="${template }/inspecao/detalhes" var="formDetalhes" />
<spring:url value="${template }/agendamento/consulta"
	var="formAgendamento" />
<spring:url value="${template }/inspecao/contato" var="formDadosPedido" />
<spring:url value="${template }/prestador/atribuir"
	var="formAtribuir" />
<spring:url
	value="${template }/reclassificacao/home"
	var="formReclassificacao" />
<spring:url value="${template }/reinspecao/home"
	var="formReinspecao" />

<html>
<head>

<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="IE=edge" http-equiv="X-UA-Compatible" />
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/bootstrap.min.css" />" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/bootstrap-theme.min.css" />" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/colorbox.css" />" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/prototipo.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/datatables.css" />" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/font-awesome.min.css" />" />

<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-1.7.2.min.js" />"></script>

</head>
<div class="container"></div>
<content tag="script"> <script>

	function habilitaCodPrestadora(){
		$("#codPrestId").css("display","block");
		$("#labelPrestId").css("display","block");		
	}
	
	function bloquearCodPrestadora(){
		$("#codPrestId").val("");
		$("#codPrestId").css("display","none");
		$("#labelPrestId").css("display","none");
	}
	
	jQuery(document).ready(function() {
		
		if($("#hiddenPrestadorId").val() == 'PRESTADOR' || $("#hiddenPrestadorId").val().trim() == ''){
			habilitaCodPrestadora();	
		} else {
			bloquearCodPrestadora();
		}

		$("#codPrestId").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
				return false;
			}
		});	
		
		$("#perfilId").change(function(){
			var perfil = $("#perfilId").val();
			
			$("#hiddenPrestadorId").val(perfil);
			
			if(perfil == 'PRESTADOR' || $("#hiddenPrestadorId").val() == 'PRESTADOR'){
				habilitaCodPrestadora();
			} else {
				bloquearCodPrestadora();
			}		
		});
		
		$("#btn_perfil").click(function(){
			var perfil = $("#perfilId").val();
			var codPrest = $("#codPrestId").val();
			
			if(perfil == 'PRESTADOR' && codPrest.trim() == '' ){
				alert("Preencha Codigo do Prestador!");
				return false;
			} else {
				return true;
			}
		});
		
		jQuery("#home").addClass("active");
		<c:if test="${not empty habilitaMenu }">
			$("#menu").css("display","block");
		</c:if>
	});
</script> </content>
<body>
<div class="container">
	<div align="left">
		<br /> <br />
		 <div class="col-md-8 col-md-offset-2 txtBox">


				<img src="http://srvprtapp001d.tokiomarine.com.br/portal_static/publico/padrao/3.1.1/images/cmm/logo-210x70.png" alt="Inspecao Inteligente - Login Teste" title="Inspecao Inteligente - Login Teste" class="center-block img-responsive" />
				<hr>
			    <form:form action="/InspecaoInteligente/home/selecionaPerfil">

				<div class="form-group col-md-12">
					<label for="sistema" class="control-label">Versao:</label> <c:out value="${cdn.versaoInspecao}" />
				</div>

				<div class="form-group col-md-6">
					<label for="usuario">Usuario</label>
					<input type="text" name="codigo" class="form-control" value="${userProfile.codigoUsuario }" />
				</div>

				<div class="form-group col-md-6">
					<label for="sistema" class="control-label">Sistema Origem</label>
					<select name="sistema" class="form-control">
						<c:forEach items="${sistemas}" var="sistema">
							<c:if test="${sistema == userProfile.sistemaOrigem }">
								<c:set var="selecionado" value=" selected=\"selected\"" />
							</c:if>
							<c:if test="${sistema != userProfile.sistemaOrigem }">
								<c:set var="selecionado" value="" />
							</c:if>
							<option value="${sistema}" label="${sistema.chaveMensagem}"
								${selecionado}>${sistema.chaveMensagem}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group col-md-6">
					<label for="perfil" class="control-label">Perfil</label>
					<select name="perfil" class="form-control" id="perfilId">
						<c:forEach items="${perfis}" var="perfil">
							<c:if test="${perfil == userProfile.perfil }">
								<c:set var="selecionado" value=" selected=\"selected\"" />
							</c:if>
							<c:if test="${perfil != userProfile.perfil }">
								<c:set var="selecionado" value="" />
							</c:if>
							<option value="${perfil}" label="${perfil.description}"
								${selecionado}>${perfil.description}</option>
						</c:forEach>
					</select>
				</div>
				
				<input type="hidden" name="hiddenPrestador" id="hiddenPrestadorId" value="${userProfile.perfil}" />
				
				<div class="form-group col-md-6">
					<label for="Cod Prestadora" id="labelPrestId">Cod. Prestadora</label>
					<input type="text" id="codPrestId" name="codPrestadora" class="form-control" value="${userProfile.codPrestadora}" />
				</div>
				
				<div class="form-group col-md-12">
					<input type="submit" value="Alterar Perfil/Usuario" class="btn btn-block btn-lg btn-success" id="btn_perfil"/>
				</div>			
				</form:form>

			<div id="menu" style="display: none;">

				<div class="form-group col-md-12">
					<label>Pacote 1 - Funcionalidades</label>
				</div>
				<!-- 	Prestador - Região Atendimento Filiais/Postos X Produto -->

				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/prestadora/cadastro/novo" method="get" id="cadastro-form">
						<input type="submit" value="Prestador - Regi&atilde;o Atendimento Filiais/Postos X Produto" class="btn btn-primary btn-lg btn-block" />
					</form:form>
				</div>

				<!-- 	Relatório de Inspeção (Grupo e Subgrupo) -->
				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/grupo/cadastro" method="get" id="cadastro-form">
						<input type="submit" value="Cadastro Grupo X SubGrupo" class="btn btn-primary btn-lg btn-block" />
					</form:form>
				</div>

				<!-- 	Relatório de Inspeção (Perguntas e Respostas) -->
				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/pergunta/cadastro" method="get" id="cadastro-form">
						<input type="submit" value="Relat&oacute;rio de Inspe&ccedil;&atilde;o (Perguntas e Respostas)" class="btn btn-primary btn-lg btn-block" />
					</form:form>
				</div>

				<!-- 	Question&aacute;rio Modelo Relat&oacute;rio -->
				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/questionario/cadastro" method="get" id="cadastro-form">
					<input type="submit" value="Question&aacute;rio Modelo Relat&oacute;rio" class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>

				<!-- 	Cadastro Modelo de Relatório -->
				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/modelo/cadastro" method="get"
						id="cadastro-form">
						<input type="submit" value="Cadastro Modelo de Relat&oacute;rio"
							class="btn btn-primary btn-lg btn-block" />
					</form:form>
				</div>

				<!-- 	Grupo de Atividade (Atividade/Sub-Atividade) -->
				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/grupoRubrica/cadastro"
						method="get" id="cadastro-form">
						<input type="submit"
							value="Grupo de Atividade (Atividade/Sub-Atividade)"
							class="btn btn-primary btn-lg btn-block" />
					</form:form>
				</div>
				<br />

				<!-- 	Cadastro Recomendacoes -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/recomendacoes/cadastro"
							method="get" id="cadastro-form">
							<input type="submit" value="Cadastro Recomendacoes"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
					</div>
				<br />

				<!-- 	Cadastro Parametro -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/parametro/cadastro"
							method="get" id="cadastro-form">
							<input type="submit" value="Cadastro Parametro"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>
				<br />

				<!-- 	Cadastro Score -->
				<div class="form-group col-md-6">
					<form:form action="/InspecaoInteligente/cadastroRangeScore/cadastro" method="get"
						id="cadastro-form">
						<input type="submit" value="Cadastro Range Score" class="btn btn-primary btn-lg btn-block" />
					</form:form>
				</div>
				<br />

				<!-- 	Componente -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/componentes" method="get">
							<input type="submit" value="Componentes" class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>


				<!-- -->
				<br /> <br />
				<div class="form-group col-md-12">
					<label>Pacote 2 - Funcionalidades</label>
				</div>


				<!--Solicitação de Inspeção Manual -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/inspecao/home" method="get"
							id="cadastro-form">
							<input type="submit"
								value="Solicita&ccedil;&atilde;o de Inspe&ccedil;&atilde;o Manual"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>
				<br />


				<!-- Consulta Solicitação -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/consulta/solicitacao"
							method="get">
							<input type="submit" value="Consulta Solicita&ccedil;&atilde;o"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>
				<br />


				<!-- Cadastro Relatório Inspeção-->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/relatorio/inspecao/home"
							method="get">
							<input type="submit"
								value="Cadastro Relat&oacute;rio Inspe&ccedil;&atilde;o"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>
				<br />


				<!-- Vincular Solicitacao -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/vincularSolicitacao/cadastro"
							method="get">
							<input type="submit"
								value="Vincular Solicita&ccedil;&atilde;o - Manual"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>
				<br />


				<!-- Baixa Recomendacao -->
				<div class="form-group col-md-6">
						<form:form action="/InspecaoInteligente/baixa/recomendacoes/cadastro"
							method="get">
							<input type="submit" value="Baixa Recomenda&ccedil;&atilde;o"
								class="btn btn-primary btn-lg btn-block" />
						</form:form>
				</div>
				<br />

				<!-- -->
				<script type="text/javascript">
					var submitSolicitacaoUrl = function(url) {
						return function() {
							var form = $('#formSolicitacao');
							form.attr('action', url);
							form.submit();
						}
					};
					$(function() {
						$('#carregar').click(
								submitSolicitacaoUrl('${fn:escapeXml(formCarregar)}'));
					});
				</script>
				<c:if test="${habilitado }">
					<c:set var="habilitacao" value="" />
					<script type="text/javascript">
						$(function() {
							$('#detalhes').click(
									submitSolicitacaoUrl('${fn:escapeXml(formDetalhes)}'));
							$('#agendamento')
									.click(
											submitSolicitacaoUrl('${fn:escapeXml(formAgendamento)}'));
							$('#dadosPedido')
									.click(
											submitSolicitacaoUrl('${fn:escapeXml(formDadosPedido)}'));
							$('#atribuir').click(
									submitSolicitacaoUrl('${fn:escapeXml(formAtribuir)}'));
							$('#reclassificar')
									.click(
											submitSolicitacaoUrl('${fn:escapeXml(formReclassificacao)}'));
							$('#reinspecao')
									.click(
											submitSolicitacaoUrl('${fn:escapeXml(formReinspecao)}'));
						});
					</script>
				</c:if>
				<c:if test="${!habilitado }">
					<c:set var="habilitacao" value=" disabled=\"disabled\"" />
				</c:if>
				<form:form modelAttribute="inp0016SolctInspc" id="formSolicitacao">

					<div class="form-group col-md-6">
						<form:input path="nrSolct" /></br>
						<input id="carregar" type="button" value="Carregar Solicitacao" class="btn btn-primary btn-lg btn-block" />
					</div>


					<!-- Detalhes de Solicitação -->
					<div class="form-group col-md-6">
						<input id="detalhes" type="button" ${habilitacao }
							value="Detalhes de Solicita&ccedil;&atilde;o"
							class="btn btn-primary btn-lg btn-block" />
					</div>


					<!-- 	Agendamento de Inspeção -->
					<div class="form-group col-md-6">
						<input id="agendamento" type="button" ${habilitacao }
							value="Agendamento Inspe&ccedil;&atilde;o" class="btn btn-primary btn-lg btn-block" />
					</div>


					<!-- 	Cadastro Dados Pedido Inspeção -->
					<div class="form-group col-md-6">
						<input id="dadosPedido" type="button" ${habilitacao }
							value="Dados Pedido Inspe&ccedil;&atilde;o (Contato)"
							class="btn btn-primary btn-lg btn-block" />

					</div>


					<!-- Atribuir Prestador -->
					<div class="form-group col-md-6">
						<input id="atribuir" type="button" ${habilitacao }
							value="Atribuir Prestador - Manual" class="btn btn-primary btn-lg btn-block" />
					</div>


					<!-- Reclassificação -->
					<div class="form-group col-md-6">
						<input id="reclassificar" type="button" ${habilitacao }
							value="Reclassifica&ccedil;&atilde;o" class="btn btn-primary btn-lg btn-block" />
					</div>


					<!-- Reinspecao -->
					<div class="form-group col-md-6">
						<input id="reinspecao" type="button" ${habilitacao }
							value="Reinspe&ccedil;&atilde;o" class="btn btn-primary btn-lg btn-block" />
					</div>

				</form:form>
			</div>
		</div>
	</div>
	</div>
 </div>
</html>
