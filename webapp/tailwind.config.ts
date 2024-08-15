import twForms from "@tailwindcss/forms"
import twTypo from "@tailwindcss/typography"
import type { Config } from "tailwindcss"

const config: Config = {
	content: ["./src/**/*.{html,js,svelte,ts}"],
	theme: {
		container: {
			center: true,
			padding: "2rem",
		},
	},
	plugins: [twTypo, twForms],
}

export default config
