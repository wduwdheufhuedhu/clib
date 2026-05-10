import { useEffect, useState } from 'react'
import { HeroGraphic } from './components/HeroGraphic'
import './App.css'

const DISCORD_URL = 'https://discord.gg/fYZt22SmTp'
const STRIPE_STANDARD = 'https://buy.stripe.com/8wM5n8eGedu67FS000'
const STRIPE_ENTERPRISE = 'https://buy.stripe.com/cN26rc2Xwdu64tGfZ1'
const AFKZONE_SPIGOT = 'https://www.spigotmc.org/resources/afkzone-afk-rewards.112656/'

const trustedNames = ['AstroPvP', 'FadeRealms', 'StrayGG', 'ZedarSMP', 'ArcanePvP']

const projects = [
  {
    title: 'WenjaPvP',
    description: 'A competitive Minecraft server focused on Hardcore Factions.',
  },
  {
    title: 'PvPTemple',
    description: 'A premier Minecraft network offering Practice, UHC, and MiniGames.',
  },
  {
    title: 'FadeRealms',
    description: 'A premier Minecraft network offering Practice, UHC, and MiniGames.',
  },
] as const

const standardFeatures = [
  'System Administration (1 machine)',
  'Initial server setup & configuration',
  'Routine software & security updates',
  'Basic performance tuning',
  'Scheduled health checks & uptime monitoring',
  'Log file management & cleanup',
] as const

const enterpriseFeatures = [
  'System Administration',
  'Advanced server deployments & scaling',
  'Database design, optimization & backups',
  'Proactive monitoring & performance tuning',
  'Custom server configuration & automation',
  'Plugin development & integration',
] as const

const team = [
  { name: 'Brendan C', role: 'Founder & Software Developer' },
  { name: 'Colton O', role: 'Project Manager' },
  { name: 'João Fernandes', role: 'Software Developer' },
  { name: 'David F', role: 'Project Manager' },
  { name: 'Nikola R', role: 'Frontend Developer' },
  { name: 'Ben Chambers', role: 'Software Developer' },
] as const

const testimonials = [
  {
    name: 'James K',
    role: 'Server Owner',
    quote:
      'The custom plugins and server optimizations are game-changing. Our server performance improved dramatically.',
  },
  {
    name: 'Tom R',
    role: 'Game Developer',
    quote:
      'Support response time is incredible - we had our Roblox game optimized and running smoothly in no time.',
  },
  {
    name: 'Marcus H',
    role: 'Community Manager',
    quote:
      'The targeted advertising campaign brought in exactly the right players. Our community quality improved significantly.',
  },
  {
    name: 'Daniel W',
    role: 'Project Lead',
    quote: 'Custom anti-cheat development exceeded our expectations. The system is both powerful and easy to maintain.',
  },
  {
    name: 'Chris M',
    role: 'Server Administrator',
    quote:
      'Development and support teams are incredibly efficient. Every ticket gets resolved with a professional touch.',
  },
] as const

const heroPlatforms = ['Minecraft', 'Roblox'] as const

function App() {
  const [heroWordIndex, setHeroWordIndex] = useState(0)

  useEffect(() => {
    const id = window.setInterval(() => {
      setHeroWordIndex((i) => (i + 1) % heroPlatforms.length)
    }, 2800)
    return () => window.clearInterval(id)
  }, [])

  return (
    <div className="app">
      <header className="header">
        <div className="header-inner">
          <a className="brand" href="/">
            <span className="brand-mark" aria-hidden="true" />
            <span className="brand-name">ConaxGames</span>
          </a>
          <nav className="nav" aria-label="Primary">
            <a href="#projects">Featured Projects</a>
            <a href="#services">Services</a>
            <a href="#team">Team</a>
            <a href="#plugins">Plugins</a>
          </nav>
        </div>
      </header>

      <div className="page">
        <main>
          <section className="hero" aria-labelledby="hero-heading">
            <div className="hero-copy">
              <p className="eyebrow">Bring Your Gaming Idea To Life</p>
              <h1 id="hero-heading">
                We create immersive
                <br />
                experiences in{' '}
                <span className="hero-highlight" aria-live="polite">
                  {heroPlatforms[heroWordIndex]}
                </span>
              </h1>
              <p className="lede">
                Our game studio designs engaging experiences for players on top platforms like Minecraft, Roblox and more
                across the world.
              </p>
              <div className="hero-meta">
                <span className="hero-stat">
                  <strong>100+</strong> happy clients
                </span>
                <span className="hero-dot" aria-hidden="true" />
                <span className="hero-stat eyebrow-inline">Featured Projects</span>
              </div>
              <div className="hero-cta">
                <a className="btn btn-solid" href={DISCORD_URL} target="_blank" rel="noopener noreferrer">
                  Let&apos;s Talk
                </a>
                <a className="btn btn-outline" href="#projects">
                  Work with us
                </a>
              </div>
            </div>
            <div className="hero-visual">
              <HeroGraphic />
            </div>
          </section>

          <section className="section trusted" aria-labelledby="trusted-heading">
            <h2 id="trusted-heading" className="section-title-center">
              Trusted by
            </h2>
            <p className="section-lead center">We are proud to work with these projects</p>
            <ul className="trusted-list">
              {trustedNames.map((name) => (
                <li key={name}>{name}</li>
              ))}
            </ul>
          </section>

          <section id="projects" className="section projects-block" aria-labelledby="projects-heading">
            <div className="section-head center">
              <h2 id="projects-heading">Featured Projects</h2>
              <p className="section-sub">Check out some of our recent Minecraft server projects</p>
            </div>
            <div className="project-grid">
              {projects.map((p) => (
                <article key={p.title} className="card project-card">
                  <h3>{p.title}</h3>
                  <p>{p.description}</p>
                  <a className="text-link" href={DISCORD_URL} target="_blank" rel="noopener noreferrer">
                    Learn More
                  </a>
                </article>
              ))}
            </div>
          </section>

          <section id="services" className="section" aria-labelledby="services-heading">
            <div className="section-head center wide">
              <h2 id="services-heading">
                Our services are <span className="hero-highlight static">available</span>
              </h2>
              <p className="section-sub">
                We offer a range of services to help you get the most out of your Minecraft server.
              </p>
            </div>

            <div className="pricing-grid">
              <article className="card pricing-card">
                <h3>Standard</h3>
                <p className="pricing-desc">The perfect plan if you&apos;re just getting started with our product.</p>
                <ul className="feature-list">
                  {standardFeatures.map((item) => (
                    <li key={item}>{item}</li>
                  ))}
                </ul>
                <p className="price">
                  <span className="price-num">$20</span>
                  <span className="price-unit">/month</span>
                </p>
                <a className="btn btn-solid btn-block" href={STRIPE_STANDARD} target="_blank" rel="noopener noreferrer">
                  Get started today
                </a>
              </article>

              <article className="card pricing-card accent">
                <h3>Enterprise</h3>
                <p className="pricing-desc">
                  The Enterprise Plan is built for growing teams needing full-scale server management.
                </p>
                <ul className="feature-list">
                  {enterpriseFeatures.map((item) => (
                    <li key={item}>{item}</li>
                  ))}
                </ul>
                <p className="price">
                  <span className="price-num">$250</span>
                  <span className="price-unit">/month</span>
                </p>
                <a
                  className="btn btn-solid btn-block"
                  href={STRIPE_ENTERPRISE}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  Get started today
                </a>
              </article>

              <div className="service-stack">
                <article className="card pricing-card compact">
                  <h3>Project Advertising</h3>
                  <p className="pricing-desc">
                    Tap into our network of anonymous, niche-targeted social media accounts to promote your project
                    without lifting a finger.
                  </p>
                  <a className="btn btn-solid btn-block" href={DISCORD_URL} target="_blank" rel="noopener noreferrer">
                    Speak to us now!
                  </a>
                </article>
                <article className="card pricing-card compact">
                  <h3>Need something more custom?</h3>
                  <p className="pricing-desc">
                    Talk to us directly and we&apos;ll craft the perfect solution for your setup.
                  </p>
                  <a className="btn btn-solid btn-block" href={DISCORD_URL} target="_blank" rel="noopener noreferrer">
                    Speak to us now!
                  </a>
                </article>
              </div>
            </div>
          </section>

          <section id="team" className="section team-section" aria-labelledby="team-heading">
            <div className="team-intro">
              <h2 id="team-heading">Our leading, strong &amp; creative team</h2>
              <p className="section-sub block">
                Our team is a passionate group of developers, designers, and gaming enthusiasts dedicated to crafting
                high-quality experiences for players worldwide. With a focus on innovation, performance, and
                community-driven development, we strive to create engaging and immersive content that brings gamers
                together.
              </p>
            </div>
            <ul className="team-grid">
              {team.map((m) => (
                <li key={m.name} className="team-card">
                  <span className="team-avatar" aria-hidden="true">
                    {m.name
                      .split(' ')
                      .map((w) => w[0])
                      .join('')}
                  </span>
                  <h3>{m.name}</h3>
                  <p>{m.role}</p>
                </li>
              ))}
            </ul>
          </section>

          <section id="plugins" className="section plugins-section invert" aria-labelledby="plugins-heading">
            <div className="section-head center wide">
              <h2 id="plugins-heading">
                Our plugins are <span className="hero-highlight static dark-bg">available</span>
              </h2>
              <p className="section-sub dark-bg">
                We offer a range of plugins to help you get the most out of your Minecraft server.
              </p>
            </div>

            <article className="plugin-showcase">
              <div className="plugin-body">
                <h3>AFKZone</h3>
                <p>
                  Boost player retention with AFKZone, a customizable AFK rewards system. Encourage participation by
                  offering incentives for staying in the AFK zone.
                </p>
                <p className="plugin-detail">
                  AFKZone enriches your Minecraft server by rewarding players for staying in designated AFK zones.
                  Features include:
                </p>
                <ul className="feature-list tight">
                  <li>Configurable reward tiers</li>
                  <li>Custom commands at milestones</li>
                  <li>Variable reward probabilities</li>
                  <li>Notification system</li>
                  <li>Multiple version support</li>
                  <li>Boosts online time and engagement</li>
                </ul>
                <div className="plugin-actions">
                  <a className="btn btn-solid" href={AFKZONE_SPIGOT} target="_blank" rel="noopener noreferrer">
                    Download
                  </a>
                  <a className="btn btn-outline dark-outline" href={AFKZONE_SPIGOT} target="_blank" rel="noopener noreferrer">
                    View More
                  </a>
                </div>
              </div>
            </article>
          </section>

          <section className="section testimonials" aria-labelledby="feedback-heading">
            <div className="section-head center wide">
              <h2 id="feedback-heading">
                23k+ Customers gave their <span className="hero-highlight static">Feedback</span>
              </h2>
              <p className="section-sub">
                Don&apos;t just take our word for it - hear what our clients have to say about their experience working with
                us.
              </p>
            </div>
            <ul className="testimonial-grid">
              {testimonials.map((t) => (
                <li key={t.name} className="card testimonial-card">
                  <div className="testimonial-avatar" aria-hidden="true">
                    {t.name[0]}
                  </div>
                  <h3>{t.name}</h3>
                  <p className="testimonial-role">{t.role}</p>
                  <blockquote className="testimonial-quote">&ldquo;{t.quote}&rdquo;</blockquote>
                </li>
              ))}
            </ul>
          </section>

          <section className="section cta-final invert" aria-labelledby="cta-heading">
            <h2 id="cta-heading">Want to take your project to the next level?</h2>
            <p className="cta-lead">
              We are a team of experienced developers who are dedicated to providing the best possible experience for our
              clients.
            </p>
            <a className="btn btn-solid btn-lg" href={DISCORD_URL} target="_blank" rel="noopener noreferrer">
              Get Started Today
            </a>
          </section>
        </main>

        <footer className="footer">
          <div className="footer-brand">
            <span className="brand-mark sm" aria-hidden="true" />
            <span>ConaxGames</span>
          </div>
          <nav className="footer-nav" aria-label="Footer">
            <span className="footer-label">Company</span>
            <a href="https://conaxgames.com/" target="_blank" rel="noopener noreferrer">
              About Us
            </a>
            <a href={DISCORD_URL} target="_blank" rel="noopener noreferrer">
              Discord
            </a>
            <a href="https://github.com/ConaxGames" target="_blank" rel="noopener noreferrer">
              GitHub
            </a>
          </nav>
          <p className="footer-meta">
            © {new Date().getFullYear()} ConaxGames · Content reflects{' '}
            <a href="https://conaxgames.com/" target="_blank" rel="noopener noreferrer">
              conaxgames.com
            </a>
          </p>
        </footer>
      </div>
    </div>
  )
}

export default App
