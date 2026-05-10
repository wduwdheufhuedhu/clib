import { ThumbIso, ThumbOrbit, ThumbStack } from './VectorArt'

const pieces = [
  {
    title: 'Nexus UI kit',
    meta: 'Product illustration · 2026',
    blurb:
      'A monochrome vector language for dashboards: states, empty views, and onboarding moments—built to stay legible from favicon to billboard.',
    Thumb: ThumbStack,
  },
  {
    title: 'Brookhaven seasonal marks',
    meta: 'Campaign artwork · limited runs',
    blurb:
      'Event key art translated into flat vector lockups, partner-safe palettes, and motion hand-offs for live-ops teams shipping weekly beats.',
    Thumb: ThumbOrbit,
  },
  {
    title: 'Driving Empire telemetry',
    meta: 'In-world HUD & badges',
    blurb:
      'Racing telemetry distilled into crisp line weights, corner radius rules, and export presets tuned for real-time rendering constraints.',
    Thumb: ThumbIso,
  },
] as const

export function FeaturedWork() {
  return (
    <section className="section" id="work" aria-labelledby="work-heading">
      <div className="section__head">
        <h2 id="work-heading" className="section__title">
          Featured vectors
        </h2>
        <p className="section__intro">
          Case-shaped blurbs in the spirit of a featured games rail—here, three libraries that show
          how we think about scale, partnerships, and polish.
        </p>
      </div>
      <ul className="card-grid">
        {pieces.map(({ title, meta, blurb, Thumb }) => (
          <li key={title}>
            <article className="card">
              <div className="card__thumb">
                <Thumb className="card__svg" />
              </div>
              <p className="card__meta">{meta}</p>
              <h3 className="card__title">{title}</h3>
              <p className="card__blurb">{blurb}</p>
              <a className="text-link" href="#insights">
                Read the breakdown
              </a>
            </article>
          </li>
        ))}
      </ul>
    </section>
  )
}
