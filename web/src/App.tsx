import { HeroGraphic } from './components/HeroGraphic'
import { IconCore, IconLattice, IconRelay, IconShield } from './components/ProductVectors'
import './App.css'

function App() {
  return (
    <div className="page">
      <header className="header">
        <a className="brand" href="/">
          <span className="brand-mark" aria-hidden="true" />
          <span className="brand-name">Nimbus</span>
        </a>
        <nav className="nav" aria-label="Primary">
          <a href="#products">Products</a>
          <a href="#ecosystem">Ecosystem</a>
          <a href="#trust">Trust</a>
        </nav>
        <div className="header-actions">
          <a className="btn btn-ghost" href="#docs">
            Docs
          </a>
          <a className="btn btn-solid" href="#download">
            Download
          </a>
        </div>
      </header>

      <main>
        <section className="hero" aria-labelledby="hero-heading">
          <div className="hero-copy">
            <p className="eyebrow">Open infrastructure</p>
            <h1 id="hero-heading">Your stack deserves the best.</h1>
            <p className="lede">
              A focused platform for running demanding workloads: tuned runtimes, a fast edge fabric, and region-aware
              scaling—built for operators who care about clarity as much as throughput.
            </p>
            <div className="hero-cta">
              <a className="btn btn-solid" href="#download">
                Get started
              </a>
              <a className="btn btn-outline" href="#docs">
                Read the docs
              </a>
            </div>
          </div>
          <div className="hero-visual">
            <HeroGraphic />
          </div>
        </section>

        <section id="products" className="section products" aria-labelledby="products-heading">
          <div className="section-head">
            <h2 id="products-heading">Three layers. One philosophy.</h2>
            <p className="section-sub">
              Choose the surface that matches your problem—each component is designed to compose cleanly with the others.
            </p>
          </div>
          <div className="product-grid">
            <article className="card">
              <IconCore className="card-icon" />
              <h3>Core</h3>
              <p>
                The primary runtime: optimized execution paths, predictable APIs, and measurable gains over vanilla
                deployments—without sacrificing compatibility with your existing toolchain.
              </p>
            </article>
            <article className="card">
              <IconRelay className="card-icon" />
              <h3>Relay</h3>
              <p>
                A high-performance mesh that routes traffic across regions and services. Connect many backends behind one
                coherent entry—built for scale-out and graceful degradation.
              </p>
            </article>
            <article className="card">
              <IconLattice className="card-icon" />
              <h3>Lattice</h3>
              <p>
                Region-scoped parallelism for workloads that need isolation without fragmentation. Better utilization on
                modern hardware while keeping behavior understandable.
              </p>
            </article>
          </div>
        </section>

        <section id="ecosystem" className="section ecosystem invert" aria-labelledby="eco-heading">
          <div className="eco-layout">
            <div className="eco-copy">
              <h2 id="eco-heading">A diverse extension ecosystem</h2>
              <p>
                Maintained by the core team and contributors, our registry is where builders publish modules and operators
                discover vetted integrations—from observability to orchestration. One place to extend the platform without
                compromising stability.
              </p>
              <a className="text-link" href="#registry">
                Browse the registry →
              </a>
            </div>
            <div className="eco-art" aria-hidden="true">
              <svg viewBox="0 0 320 240" xmlns="http://www.w3.org/2000/svg">
                <g stroke="#050505" fill="none" strokeWidth="1.15" strokeLinecap="round">
                  <rect x="24" y="32" width="120" height="72" rx="3" />
                  <rect x="176" y="48" width="120" height="72" rx="3" opacity="0.85" />
                  <rect x="100" y="136" width="120" height="72" rx="3" opacity="0.7" />
                  <path d="M84 68 H152 M236 84 H284 M160 172 H220" opacity="0.5" />
                  <circle cx="160" cy="68" r="5" fill="#050505" />
                  <circle cx="236" cy="120" r="5" fill="#050505" opacity="0.85" />
                  <circle cx="160" cy="172" r="5" fill="#050505" opacity="0.7" />
                </g>
              </svg>
            </div>
          </div>
        </section>

        <section className="section stats" aria-labelledby="stats-heading">
          <h2 id="stats-heading" className="sr-only">
            Scale and reach
          </h2>
          <ul className="stat-row">
            <li>
              <span className="stat-value">480k+</span>
              <span className="stat-label">Active deployments daily</span>
            </li>
            <li>
              <span className="stat-value">62ms</span>
              <span className="stat-label">p99 edge overhead</span>
            </li>
            <li>
              <span className="stat-value">140+</span>
              <span className="stat-label">Countries with presence</span>
            </li>
          </ul>
          <p className="stat-note">
            From single-node labs to sprawling fleets, the same toolchain stays boring on purpose—so your incidents are
            rare and your upgrades are calm.
          </p>
        </section>

        <section id="trust" className="section promise" aria-labelledby="trust-heading">
          <div className="promise-inner">
            <IconShield className="promise-icon" />
            <div>
              <h2 id="trust-heading">The promise of stability</h2>
              <p>
                Security and resilience are not optional extras. We ship fixes quickly, publish clear advisories, and design
                defaults that reduce misconfiguration—so you can spend energy on product work instead of firefighting.
              </p>
            </div>
          </div>
        </section>
      </main>

      <footer className="footer">
        <div className="footer-brand">
          <span className="brand-mark sm" aria-hidden="true" />
          <span>Nimbus</span>
        </div>
        <nav className="footer-nav" aria-label="Footer">
          <a href="#docs">Documentation</a>
          <a href="#download">Downloads</a>
          <a href="#community">Community</a>
        </nav>
        <p className="footer-meta">© {new Date().getFullYear()} Nimbus · Dummy landing · Inspired by papermc.io tone</p>
      </footer>
    </div>
  )
}

export default App
