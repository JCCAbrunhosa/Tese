<?php
/**
 * The template for displaying all pages
 *
 * This is the template that displays all pages by default.
 * Please note that this is the WordPress construct of pages
 * and that other 'pages' on your WordPress site may use a
 * different template.
 *
 * @link https://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Twenty_Seventeen
 * @since 1.0
 * @version 1.0
 */

get_header(); ?>

<div class="main-div" id="main-div">
  <input type="email" placeholder="Email" id="email" />
  <input type="password" placeholder="password" id="pwd"/>
  <a id="registar" onclick="hideLogin()">Não tem uma conta? Registe-se!</a>
  <br>
  <label id="pro"style="display:none"><input type="checkbox" id ="checkPro"onchange="hideLogin()"/>Profissional</label>
  <br>
  <input type="userName" style="display:none" placeholder="Nome de Utilizador" id="userName" />

  <br>
  <button id ="login" onclick="login()">Login</button>
  <br>
  <button id ="register" style="display:none" onclick="register()">Registar</button>
  <br>
  <button id ="cancelRegister" style="display:none" onclick="cancelRegister()">Cancelar</button>
</div>


<?php get_footer();
