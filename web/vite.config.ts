import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  preview: {
    host: true,
    /** Allow Cloudflare quick tunnels (*.trycloudflare.com) and similar proxies */
    allowedHosts: true,
  },
})
