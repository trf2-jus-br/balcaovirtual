import decode from "jwt-decode";

const ID_TOKEN_KEY = "bv-jwt";

export default {
  logout: function() {
    this.clearIdToken();
  },

  getCookie: function(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
  },

  getIdToken: function() {
    return this.getCookie(ID_TOKEN_KEY);
  },

  clearIdToken: function() {
    document.cookie = ID_TOKEN_KEY + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  },

  // Get and store id_token in local storage
  setIdToken: function(idToken) {
  },

  isLoggedIn: function() {
    const idToken = this.getIdToken();
    return !!idToken && !this.isTokenExpired(idToken);
  },

  decodeToken: function(encodedToken) {
    var decoded = decode(encodedToken);
    if (decoded && decoded.users) {
      var a = decoded.users.split(";");
      decoded.user = {};
      for (var i = 0; i < a.length; i++) {
        var b = a[i].split(",");
        decoded.user[b[0]] = {
          sistema: b[0],
          ieusu: b[1],
          origin: b[2],
          ieentidade: b[3] === "null" ? undefined : b[3],
          entidade: b[4] === "null" ? undefined : b[4],
          ieunidade: b[5] === "null" ? undefined : b[5],
          unidade: b[6] === "null" ? undefined : b[6],
          perfil: b[7] === "null" ? undefined : b[7]
        };
      }
    }
    decoded.company =
      decoded.email !== null ? decoded.email.split("@")[1] : undefined;
    decoded.isInterno = sistema =>
      decoded.user[sistema] ? decoded.user[sistema].origin === "int" : false;
    decoded.isExterno = sistema =>
      decoded.user[sistema] ? decoded.user[sistema].origin === "ext" : false;
    decoded.isMagistrado = () => {
      for (var sistema in decoded.user) {
        if (
          decoded.user.hasOwnProperty(sistema) &&
          decoded.user[sistema].perfil === "magistrado"
        )
          return true;
      }
      return false;
    };
    return decoded;
  },

  getTokenExpirationDate: function(encodedToken) {
    const token = decode(encodedToken);
    if (!token.exp) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(token.exp);

    return date;
  },

  isTokenExpired: function(token) {
    const expirationDate = this.getTokenExpirationDate(token);
    return expirationDate < new Date();
  }
};
