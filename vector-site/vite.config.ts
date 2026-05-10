import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // Cloudflare quick tunnels use random *.trycloudflare.com hosts (vite preview host check).
  preview: {
    allowedHosts: ['.trycloudflare.com'],
  },
  server: {
    allowedHosts: ['.trycloudflare.com'],
  },
})
