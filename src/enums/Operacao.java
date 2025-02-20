package enums;

public enum Operacao {
	//Login
	login,
	logout,
	
	//Usuario
	localizarUsuario,
	listarUsuarios,
	cadastrarUsuario,
	editarUsuario,
	excluirUsuario,

	//Categoria
	localizarCategoria,
	listarCategorias,
	salvarCategoria,
	excluirCategoria,
	listarUsuarioCategorias,
	cadastrarUsuarioCategoria,
	descadastrarUsuarioCategoria,
	
	//Avisos
	localizarAviso,
	listarAvisos,
	salvarAviso,
	excluirAviso,
	listarUsuarioAvisos,
	
	//Erro
	naoEncontrada
}
