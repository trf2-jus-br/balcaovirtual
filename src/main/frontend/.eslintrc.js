module.exports = {
    root: true,
    env: {
        node: true
    },
    rules: {
        "prettier/prettier": ["warn", {
            "printWidth": 180,
        }],
        "no-unused-vars": ["error", {
            args: "none"
        }],
        "no-console": process.env.NODE_ENV === "production" ? "error" : "off",
        "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
        "vue/require-v-for-key": "off"
    },
    parserOptions: {
        parser: "babel-eslint"
    },
    extends: ["plugin:vue/essential", "@vue/prettier", "eslint:recommended"]
};