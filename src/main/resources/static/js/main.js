import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'api/resource'
import App from 'pages/App.vue'
import store from 'state/store.js'
import { connect } from 'util/ws.js'
import 'vuetify/dist/vuetify.min.css'


if (frontendData.profile) {
    connect();
} 

Vue.use(Vuetify)

new Vue({
	el: '#app',
	vuetify: new Vuetify(),
	store,
	render: a => a(App)
})