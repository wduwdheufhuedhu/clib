import { HeroComposition } from './VectorArt'

export function Hero() {
  return (
    <section className="hero" id="top" aria-labelledby="hero-heading">
      <div className="hero__copy">
        <p className="eyebrow">2D vector artwork · identity · systems</p>
        <h1 id="hero-heading" className="hero__title">
          Sharp geometry for products people live inside.
        </h1>
        <p className="hero__lede">
          We partner with studios and platform teams to ship illustration languages, UI marks, and
          campaign-ready vector systems—crafted for clarity at every scale, from app chrome to
          out-of-home.
        </p>
        <div className="hero__actions">
          <a className="btn btn--solid" href="#work">
            View selected work
          </a>
          <a className="btn btn--ghost" href="#careers">
            Join the collective
          </a>
        </div>
        <dl className="hero__stats">
          <div>
            <dt>Monthly reach</dt>
            <dd>140M+</dd>
          </div>
          <div>
            <dt>Vector libraries shipped</dt>
            <dd>48</dd>
          </div>
          <div>
            <dt>Median hand-off time</dt>
            <dd>11 days</dd>
          </div>
        </dl>
      </div>
      <div className="hero__art" aria-hidden>
        <HeroComposition className="hero__svg" />
      </div>
    </section>
  )
}
