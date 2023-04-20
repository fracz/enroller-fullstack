const {defineConfig} = require('@vue/cli-service')

module.exports = defineConfig({
    devServer: {
        port: 9000,
        proxy: {
            "^/api": {
                "target": "http://localhost:8080"
            }
        }
    }
});
