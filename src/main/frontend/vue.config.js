const path = require("path");

module.exports = {
  outputDir: path.resolve(__dirname, "../webapp"),
  assetsDir: "static",
  publicPath: process.env.NODE_ENV === "production" ? "/balcaojus/" : "/",
  lintOnSave: true,
  runtimeCompiler: true,
  pwa: {
    name: "My App",
    themeColor: "#212529",
    msTileColor: "#000000",
    appleMobileWebAppCapable: "yes",
    appleMobileWebAppStatusBarStyle: "black",
    assetsVersion: "",
    manifestOptions: {
      short_name: "Balcãojus",
      start_url: "/balcaojus/index.html",
      description: "Interface para acesso aos sistemas processuais.",
      background_color: "#212529",
      "theme-color": "#212529"
    },

    // configure the workbox plugin
    workboxPluginMode: "GenerateSW",
    workboxOptions: {
      skipWaiting: true
    },
    iconPaths: {
      favicon32: "img/icons/favicon-32x32.png",
      favicon16: "img/icons/favicon-16x16.png",
      appleTouchIcon: "img/icons/apple-touch-icon-152x152.png",
      maskIcon: "img/icons/safari-pinned-tab.svg",
      msTileImage: "img/icons/msapplication-icon-144x144.png"
    }
  },
  devServer: {
    disableHostCheck: true
  }
};
