import './App.css'
import {
  HeroComposition,
  CapabilityMark,
  SectionRule,
} from './components/VectorArt.jsx'

const nav = [
  { href: '#capabilities', label: 'Capabilities' },
  { href: '#delivery', label: 'Delivery' },
  { href: '#security', label: 'Security' },
  { href: '#contact', label: 'Contact' },
]

const capabilities = [
  {
    title: 'IT operations & reliability',
    body:
      'Service ownership, observability, and incident lifecycle management aligned to your tiers, regions, and contractual commitments.',
    mark: 0,
  },
  {
    title: 'Managed security operations',
    body:
      'Continuous monitoring, threat intelligence correlation, and coordinated response that extends your team without replacing it.',
    mark: 1,
  },
  {
    title: 'GRC programs',
    body:
      'Policy frameworks, control mapping, and audit-ready evidence designed for regulated environments and board-level reporting.',
    mark: 2,
  },
  {
    title: 'Offensive security',
    body:
      'Controlled adversary simulation, vulnerability management, and remediation prioritization grounded in business impact.',
    mark: 3,
  },
]

const phases = [
  {
    step: '01',
    title: 'Assess',
    text:
      'We baseline your operating model, control environment, and signal quality to define measurable outcomes.',
  },
  {
    step: '02',
    title: 'Design',
    text:
      'Runbooks, RACI, and integration patterns are co-authored so delivery matches how your organization actually works.',
  },
  {
    step: '03',
    title: 'Operate',
    text:
      '24/7 coverage where required, with transparent reporting and escalation paths that leadership can trust.',
  },
  {
    step: '04',
    title: 'Evolve',
    text:
      'Quarterly posture reviews and roadmap refinements keep programs aligned to risk, regulation, and growth.',
  },
]

export default function App() {
  return (
    <div className="site">
      <a className="skip" href="#main">
        Skip to content
      </a>

      <header className="top">
        <div className="shell top__inner">
          <a className="mark" href="#">
            VaultX
            <span className="mark__suffix">Services</span>
          </a>
          <nav className="nav" aria-label="Primary">
            <ul>
              {nav.map((item) => (
                <li key={item.href}>
                  <a href={item.href}>{item.label}</a>
                </li>
              ))}
            </ul>
          </nav>
        </div>
      </header>

      <main id="main">
        <section className="hero shell" aria-labelledby="hero-heading">
          <div className="hero__copy">
            <p className="eyebrow">Enterprise technology services</p>
            <h1 id="hero-heading" className="display">
              Operational clarity at enterprise scale.
            </h1>
            <p className="lede">
              VaultX Services partners with teams who run critical infrastructure—from
              managed detection and response to governance, risk, and compliance programs.
              We combine disciplined engineering with security operations that respect how
              your business actually runs.
            </p>
            <div className="hero__actions">
              <a className="btn btn--primary" href="#contact">
                Start a conversation
              </a>
              <a className="btn btn--ghost" href="#capabilities">
                View capabilities
              </a>
            </div>
          </div>
          <div className="hero__art" aria-hidden>
            <HeroComposition className="hero__svg" />
          </div>
        </section>

        <div className="shell rule-wrap">
          <SectionRule className="section-rule" />
        </div>

        <section
          id="capabilities"
          className="band band--muted"
          aria-labelledby="cap-heading"
        >
          <div className="shell">
            <div className="section-head">
              <h2 id="cap-heading" className="h2">
                Capabilities
              </h2>
              <p className="section-head__meta">
                End-to-end programs across operations, security, and compliance—with a
                single accountable delivery model.
              </p>
            </div>
            <ul className="grid-cap">
              {capabilities.map((c) => (
                <li key={c.title} className="card">
                  <CapabilityMark variant={c.mark} className="card__mark" />
                  <h3 className="h3">{c.title}</h3>
                  <p>{c.body}</p>
                </li>
              ))}
            </ul>
          </div>
        </section>

        <section id="delivery" className="band band--dark" aria-labelledby="del-heading">
          <div className="shell band--dark__inner">
            <div className="split">
              <div>
                <p className="eyebrow eyebrow--on-dark">Delivery model</p>
                <h2 id="del-heading" className="h2 h2--on-dark">
                  Built for regulated and high-availability environments.
                </h2>
              </div>
              <p className="split__aside">
                We embed alongside your teams: shared tooling, agreed SLAs, and documentation
                that survives audits and handovers. No black boxes—every control and workflow
                is visible in your systems of record.
              </p>
            </div>
            <ol className="phases">
              {phases.map((p) => (
                <li key={p.step} className="phase">
                  <span className="phase__step">{p.step}</span>
                  <div>
                    <h3 className="h3 h3--on-dark">{p.title}</h3>
                    <p className="phase__text">{p.text}</p>
                  </div>
                </li>
              ))}
            </ol>
          </div>
        </section>

        <section
          id="security"
          className="band"
          aria-labelledby="sec-heading"
        >
          <div className="shell">
            <div className="section-head section-head--tight">
              <h2 id="sec-heading" className="h2">
                Security posture by design
              </h2>
              <p className="section-head__meta">
                Least-privilege access, segregated environments, and cryptographic protection
                for data in transit and at rest. Our engagements map cleanly to common
                frameworks so evidence collection is continuous—not a scramble before audit.
              </p>
            </div>
            <ul className="pill-row">
              <li>SOC 2–aligned controls</li>
              <li>ISO 27001 mapping</li>
              <li>Incident retainment &amp; chain of custody</li>
              <li>Regional data residency options</li>
            </ul>
          </div>
        </section>

        <section id="contact" className="cta" aria-labelledby="cta-heading">
          <div className="shell cta__inner">
            <h2 id="cta-heading" className="h2 h2--on-dark">
              Tell us what you are protecting.
            </h2>
            <p className="cta__lede">
              Share context on your stack, compliance drivers, and coverage gaps. We will
              respond with a concise assessment plan—usually within two business days.
            </p>
            <a className="btn btn--on-dark" href="mailto:hello@vaultx.services">
              hello@vaultx.services
            </a>
          </div>
        </section>
      </main>

      <footer className="foot">
        <div className="shell foot__inner">
          <p className="foot__brand">
            VaultX Services
            <span className="foot__note">© {new Date().getFullYear()}</span>
          </p>
          <p className="foot__legal">
            Illustrations are inline SVG. This page uses placeholder contact details for
            demonstration.
          </p>
        </div>
      </footer>
    </div>
  )
}
