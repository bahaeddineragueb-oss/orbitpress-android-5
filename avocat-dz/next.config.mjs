/** @type {import('next').NextConfig} */
const isElectron = process.env.ELECTRON === 'true';

const nextConfig = {
  reactStrictMode: true,
  // للـ PC (Electron) نحتاج تصدير Static
  ...(isElectron ? {
    output: 'export',
    distDir: 'out',
    images: { unoptimized: true },
    trailingSlash: true,
  } : {
    images: { unoptimized: false },
  }),
  // لمنع مشاكل file:// في Electron
  assetPrefix: isElectron ? './' : undefined,
};

export default nextConfig;
