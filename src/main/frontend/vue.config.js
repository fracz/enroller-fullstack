module.exports = {
    devServer: {
        port: 9000,
        proxy: {
            "^/api": {
                "target": "http://localhost:8080"
            }
        }
    }
};