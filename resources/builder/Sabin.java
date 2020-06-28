package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sabin extends _Default{
	public Sabin() {
		super("Sabin", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene", "rene");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5983};
				break;
			case HP:
				ids = new int[]{5996};
				break;
			case S1:
				ids = new int[]{5982, 5981};
				break;
			case S2:
				ids = new int[]{5987, 5986};
				break;
			case EX:
				ids = new int[]{5993, 5992};
				break;
			case AA:
				ids = new int[]{5967};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}