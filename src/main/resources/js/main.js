import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'api/resource'
import router from 'router/router.js'
import App from 'pages/App.vue'
import store from 'state/store.js'
import { connect } from 'util/ws.js'
import 'vuetify/dist/vuetify.min.css'


if (profile) {
    connect();
} 

Vue.use(Vuetify)

new Vue({
	el: '#app',
	vuetify: new Vuetify(),
	store,
	router,
	render: a => a(App)
})