<script lang="ts">
	import type { Snippet } from "svelte"
	import "../app.css"

	interface Props {
		children: Snippet
	}
	let { children }: Props = $props()
</script>

<svelte:head>
	<title>Quiz App</title>
	<meta
		name="description"
		content="Take our quiz, see if you can get them all right!"
	/>
</svelte:head>

<div class="prose lg:prose-lg container my-16 space-y-16">
	{@render children()}
</div>
