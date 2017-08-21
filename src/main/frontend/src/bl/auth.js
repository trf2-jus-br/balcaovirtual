import Vue from 'vue'
import decode from 'jwt-decode'

const ID_TOKEN_KEY = 'id_token'

export default {
  logout: function () {
    this.clearIdToken()
  },

  getIdToken: function () {
    return localStorage.getItem(ID_TOKEN_KEY)
  },

  clearIdToken: function () {
    localStorage.removeItem(ID_TOKEN_KEY)
  },

  // Get and store id_token in local storage
  setIdToken: function (idToken) {
    localStorage.setItem(ID_TOKEN_KEY, idToken)
    Vue.http.headers.common['Authorization'] = 'Bearer ' + idToken
  },

  isLoggedIn: function () {
    const idToken = this.getIdToken()
    return !!idToken && !this.isTokenExpired(idToken)
  },

  decodeToken: function (encodedToken) {
    return decode(encodedToken)
  },

  getTokenExpirationDate: function (encodedToken) {
    const token = decode(encodedToken)
    if (!token.exp) {
      return null
    }

    const date = new Date(0)
    date.setUTCSeconds(token.exp)

    return date
  },

  isTokenExpired: function (token) {
    const expirationDate = this.getTokenExpirationDate(token)
    return expirationDate < new Date()
  }
}
