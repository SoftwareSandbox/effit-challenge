module.exports = {
    devServer: {
        proxy: {
            "/api": {
                target: "http://localhost:8080",
                changeOrigin: true
            }
        },
        port: 8081
    },
    publicPath: process.env.PUBLIC_PATH || '/',
    assetsDir: process.env.ASSETS_DIR || ''
};
