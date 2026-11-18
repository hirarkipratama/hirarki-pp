const fs = require('fs');
const path = require('path');

function findAssetsSource() {
    const roots = [process.cwd(), typeof __dirname === 'undefined' ? process.cwd() : __dirname];
    for (const root of roots) {
        let current = root;
        for (let depth = 0; depth < 6; depth++) {
            const candidate = path.resolve(current, 'src/wasmJsMain/resources/assets');
            if (fs.existsSync(candidate)) return candidate;
            current = path.dirname(current);
        }
    }
    return null;
}

config.plugins.push({
    apply(compiler) {
        const source = findAssetsSource();
        if (!source) return;
        compiler.hooks.afterEmit.tap('CopyPortfolioAssets', () => {
            const destination = path.resolve(compiler.options.output.path, 'assets');
            fs.cpSync(source, destination, { recursive: true, force: true });
        });
    }
});
